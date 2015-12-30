package controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jsonobjects.OpinionJson;
import models.Api;
import models.Opinion;
import models.Usuario;
import helpers.ControllerHelper;
import play.cache.Cache;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Opiniones extends Controller{
	
	/**
	 * Action method para obtener el listado de las opiniones de un usuario
	 * parametro opcional: tamPagina:  numero de registros maximo a devolver. Si se manda un 0 o null se devuelven 10
	 * parametro opcional: pagina:  numero de pagina donde comenzar la devolucion de registros. Si se manda un 0 o null se devuelve la primera pagina
	 * @param uId id del usuario del cual queremos obtener las opiniones 
	 */
	public Result listadoOpinionesUsuario(Long uId) {
		//antes de hacer nada comprobamos si el cliente acepta nuestros formatos de respuesta
		if(!ControllerHelper.soportaFormatosAPI(request())){
			return badRequest(ControllerHelper.errorJson(1, "error.format", null));
		}		
		Result res = null;

		//comprobamos si nos han pasado la pagina y el tamaño
		Integer tamPagina = ControllerHelper.getTamPagina(request().getQueryString("tamPagina"));
		Integer pagina = ControllerHelper.getPagina(request().getQueryString("pagina"));
		if (tamPagina == null || pagina == null){
			return badRequest(ControllerHelper.errorJson(2, "error.paginacion", null));
		}

		
		Usuario usuario = Usuario.find.byId(uId);
		if (usuario == null) {
			return notFound();
		}
		
		Integer totalRegistros = usuario.getOpiniones().size();

		List<Opinion> opiniones = new ArrayList<Opinion>(); 
		if(ControllerHelper.recuperarCache(tamPagina, pagina)){
			opiniones = Cache.getOrElse("opinionesUsuario" + usuario.id, () -> usuario.getOpinionesPaginado(tamPagina, pagina), 60*60*24);
		}else{
			opiniones = usuario.getOpinionesPaginado(tamPagina, pagina);
		}
		
		if (ControllerHelper.soportaJson(request())) {
			res = getResultado(ControllerHelper.getListOpinionJson(opiniones), totalRegistros);
		}else if (ControllerHelper.soportaXml(request())) {
			res = ok(views.xml.opiniones.render(opiniones,totalRegistros));
		}
		
		return res; 
	}
	
	/**
	 * Action method para obtener un listado de las opiniones de un API
	 * parametro opcional: tamPagina:  numero de registros maximo a devolver. Si se manda un 0 o null se devuelven 10
	 * parametro opcional: pagina:  numero de pagina donde comenzar la devolucion de registros. Si se manda un 0 o null se devuelve la primera pagina
	 * @param aId id de la api de la cual queremos obtener las opiniones 
	 */
	public Result listadoOpinionesApi(Long aId) {
		//antes de hacer nada comprobamos si el cliente acepta nuestros formatos de respuesta
		if(!ControllerHelper.soportaFormatosAPI(request())){
			return badRequest(ControllerHelper.errorJson(1, "error.format", null));
		}
		Result res = null;

		//comprobamos si nos han pasado la pagina y el tamaño
		Integer tamPagina = ControllerHelper.getTamPagina(request().getQueryString("tamPagina"));
		Integer pagina = ControllerHelper.getPagina(request().getQueryString("pagina"));
		if (tamPagina == null || pagina == null){
			return badRequest(ControllerHelper.errorJson(2, "error.paginacion", null));
		}		
		
		Api api = Api.find.byId(aId);
		if (api == null) {
			return notFound();
		}
		
		Integer totalRegistros = api.getOpiniones().size();
		
		List<Opinion> opiniones = new ArrayList<Opinion>(); 
		if(ControllerHelper.recuperarCache(tamPagina, pagina)){
			opiniones = Cache.getOrElse("opinionesApi" + api.id, () -> api.getOpinionesPaginado(tamPagina, pagina), 60*60*24);
		}else{
			opiniones = api.getOpinionesPaginado(tamPagina, pagina);
		}
		
		if (ControllerHelper.soportaJson(request())) {
			res = getResultado(ControllerHelper.getListOpinionJson(opiniones), totalRegistros);
		}else if (ControllerHelper.soportaXml(request())) {
			res = ok(views.xml.opiniones.render(opiniones,totalRegistros));
		}
		
		return res;
	}
	
	
	/**
	 * Action method para dar de alta una opinion nueva en el sistema
	 * Se deben pasar los atributos de la opinion en el body de la peticion.  
	 * @param 
	 */
	public Result crearOpinion() {
		//antes de hacer nada comprobamos si el cliente acepta nuestros formatos de respuesta
		if(!ControllerHelper.soportaFormatosAPI(request())){
			return badRequest(ControllerHelper.errorJson(1, "error.format", null));
		}		
	
		Form<Opinion> form = Form.form(Opinion.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorJson(2, "error.opinion", form.errorsAsJson()));
		}

		Opinion opinion = form.get();
		
		
		//actualizamos la puntuacion media de la API
		Integer suma = opinion.getPuntuacion();
		Integer contador = 1;
		
		Usuario usuario = Usuario.find.byId(opinion.getUsuario().id);		
		if (usuario == null) {
			return badRequest(ControllerHelper.errorJson(1, "error.usuario.inexistente", null));
		}
		
		Api api = Api.find.byId(opinion.getApi().id);
		if (api == null) {
			return badRequest(ControllerHelper.errorJson(1, "error.api.inexistente", null));
		}
		
		Iterator<Opinion> opinionesMedia = api.getOpiniones().iterator();
		while(opinionesMedia.hasNext()){
			Opinion opinionMedia = opinionesMedia.next();
			suma += opinionMedia.getPuntuacion();
			contador++;
		}
		BigDecimal media = new BigDecimal(suma/contador);
		api.setPuntuacionMedia(media);
		api.save();
		
		//borramos la api de cache ya que cambia su nota media y su lista de opiniones
		//borramos los listados de apis de cache
		//borramos las opiniones de este usuario
		//borramos las opiniones de esta api
		List<String> listaCachesBorrar = new ArrayList<String>();
		listaCachesBorrar.add("api" + api.id);
		listaCachesBorrar.add("listadoApis");
		listaCachesBorrar.add("listadoGratis");
		listaCachesBorrar.add("opinionesApi" + api.id);
		listaCachesBorrar.add("opinionesUsuario" + opinion.getUsuario().id);
		ControllerHelper.borrarCache(listaCachesBorrar);
		
		
		
		//actualizamos el api en la opinion
		opinion.setApi(api);
		opinion.setUsuario(usuario);
		opinion.save();
		
		Result res = null;

		if (ControllerHelper.soportaJson(request())) {
			res = ok(Json.toJson(ControllerHelper.getOpinionJson(opinion)));
		}else if (ControllerHelper.soportaXml(request())) {
			res = ok(views.xml._opinion.render(opinion));
		}
		
		return res;
	}
	
	/**
	 * Action method para borrar una opinion del sistema
	 * @param oId id de la opinion
	 */
	public Result borrarOpinion(Long oId) {
		//antes de hacer nada comprobamos si el cliente acepta nuestros formatos de respuesta
		if(!ControllerHelper.soportaFormatosAPI(request())){
			return badRequest(ControllerHelper.errorJson(1, "error.format", null));
		}		
		Opinion opinion = Opinion.find.byId(oId);
		
		
		
		if (opinion == null) {
			return notFound();
		}
		
		
		Api api = Api.find.byId(opinion.getApi().id);
		
		//actualizamos la puntuacion media de la API
		Integer suma = opinion.getPuntuacion();
		Integer contador = 0;
		
		Iterator<Opinion> opinionesMedia = api.getOpiniones().iterator();
		while(opinionesMedia.hasNext()){
			Opinion opinionMedia = opinionesMedia.next();
			suma += opinionMedia.getPuntuacion();
			contador++;
		}
		BigDecimal media = new BigDecimal(suma/contador);
		api.setPuntuacionMedia(media);
		api.save();
		
		//borramos la api de cache ya que cambia su nota media y su lista de opiniones
		//borramos los listados de apis de cache
		//borramos las opiniones de este usuario
		//borramos las opiniones de esta api
		List<String> listaCachesBorrar = new ArrayList<String>();
		listaCachesBorrar.add("api" + api.id);
		listaCachesBorrar.add("listadoApis");
		listaCachesBorrar.add("listadoGratis");
		listaCachesBorrar.add("opinionesApi" + api.id);
		listaCachesBorrar.add("opinionesUsuario" + opinion.getUsuario().id);
		ControllerHelper.borrarCache(listaCachesBorrar);
		
		opinion.delete();
		
		return ok();
	}
	
	
	private Result getResultado(List<OpinionJson> listaOpiniones, Integer totalRegistros){
		Map<String, Object> resultado = new HashMap<String, Object>();
		resultado.put("totalRegistros", totalRegistros);
		resultado.put("datos", listaOpiniones);
		return ok(Json.toJson(resultado));
	}
	
}
