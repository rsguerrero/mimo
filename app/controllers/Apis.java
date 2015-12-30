package controllers;

import helpers.ControllerHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Api;
import play.cache.Cache;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Apis extends Controller{
	
	/**
	 * Action method para obtener la informacion sobre un API en concreto
	 * @param aId id de la API a consultar
	 */
	public Result informacionAPI(Long aId) {
		//antes de hacer nada comprobamos si el cliente acepta nuestros formatos de respuesta
		if(!ControllerHelper.soportaFormatosAPI(request())){
			return badRequest(ControllerHelper.errorJson(1, "error.format", null));
		}
		Result res = null;
		//lo guardamos en cache con el nombre api + id del Api. Lo guardamos el dia entero. 
		//La borraremos de la cache cuando se cree o se borre una opinion  para esta api ya que cambia su lista de opiniones y su nota media
		Api api = Cache.getOrElse("api" + aId, () -> Api.findApi(aId), 60*60*24);
		if (api == null) {
			res = notFound();
		}else{
			if (ControllerHelper.soportaJson(request())) {
				res = ok(Json.toJson(api));
			}else if (ControllerHelper.soportaXml(request())) {
				res = ok(views.xml._api.render(api));
			}
		}

		return res; 
	}
	
	/**
	 * Action method para obtener un listado de apis
	 * parametro opcional: tamPagina:  numero de registros maximo a devolver. Si se manda un 0 o null se devuelven 10
	 * parametro opcional: pagina:  numero de pagina donde comenzar la devolucion de registros. Si se manda un 0 o null se devuelve la primera pagina
	 */
	public Result listado() {
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
		
		Integer totalRegistros = totalApis();
		List<Api> listaApis = new ArrayList<Api>(); 
		if(ControllerHelper.recuperarCache(tamPagina, pagina)){
			listaApis = Cache.getOrElse("listadoApis", () -> Api.findApisLimitado(tamPagina, pagina), 60*60*24);
		}else{
			listaApis = Api.findApisLimitado(tamPagina, pagina);
		}
		if (ControllerHelper.soportaJson(request())) {
			res = getResultado(listaApis, totalRegistros);
		}else if (ControllerHelper.soportaXml(request())) {
			res = ok(views.xml.apis.render(listaApis,totalRegistros));
		}
		
		return res; 
	}
	
	/**
	 * Action method para obtener las APIS con informacion sobre una determinada tematica
	 * parametro opcional: tamPagina:  numero de registros maximo a devolver. Si se manda un 0 o null se devuelven 10
	 * parametro opcional: pagina:  numero de pagina donde comenzar la devolucion de registros. Si se manda un 0 o null se devuelve la primera pagina
	 * 
	 * @param tematica tematica de la informacion que contiene la API
	 */
	public Result listadoTematica(String tematica) {
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
				
		Integer totalRegistros = totalApis();
		List<Api> listaApis;
		listaApis = Api.findApisTematicaLimitada(tamPagina, pagina, tematica);
		
		if (ControllerHelper.soportaJson(request())) {
			res = getResultado(listaApis, totalRegistros);
		}else if (ControllerHelper.soportaXml(request())) {
			res = ok(views.xml.apis.render(listaApis,totalRegistros));
		}
		
		return res;
	}
	
	/**
	 * Action method para obtener las APIS que devuelven la respuesta en un formato en concreto
	 * parametro opcional: tamPagina:  numero de registros maximo a devolver. Si se manda un 0 o null se devuelven 10
	 * parametro opcional: pagina:  numero de pagina donde comenzar la devolucion de registros. Si se manda un 0 o null se devuelve la primera pagina
	 * 
	 * @param formatoRespuesta formato de respuesta que aceptarán las API devueltas
	 */
	public Result listadoFormato(String formatoRespuesta) {
		//antes de hacer nada comprobamos si el cliente acepta nuestros formatos de respuesta
		if(!ControllerHelper.soportaFormatosAPI(request())){
			return badRequest(ControllerHelper.errorJson(1, "error.format", null));
		}
		//comprobamos que el formato que han pasado como parametros es uno de los soportados en las APIS
		if(!Api.comprobarFormatos(formatoRespuesta)){
			return badRequest(ControllerHelper.errorJson(2, "error.formato.api", null));
		}
	
	
		Result res = null;

		//comprobamos si nos han pasado la pagina y el tamaño
		Integer tamPagina = ControllerHelper.getTamPagina(request().getQueryString("tamPagina"));
		Integer pagina = ControllerHelper.getPagina(request().getQueryString("pagina"));
		if (tamPagina == null || pagina == null){
			return badRequest(ControllerHelper.errorJson(2, "error.paginacion", null));
		}
		
		Integer totalRegistros = totalApis();
		List<Api> listaApis;
		listaApis = Api.findApisFormatoLimitada(tamPagina, pagina, formatoRespuesta);
		
		if (ControllerHelper.soportaJson(request())) {
			res = getResultado(listaApis, totalRegistros);
		}else if (ControllerHelper.soportaXml(request())) {
			res = ok(views.xml.apis.render(listaApis,totalRegistros));
		}
		
		return res;
	}
	
	/**
	 * Action method para obtener las APIS que son gratis
	 * parametro opcional: tamPagina:  numero de registros maximo a devolver. Si se manda un 0 o null se devuelven 10
	 * parametro opcional: pagina:  numero de pagina donde comenzar la devolucion de registros. Si se manda un 0 o null se devuelve la primera pagina
	 * 
	 */
	public Result listadoGratis() {
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
		
		Integer totalRegistros = totalApis();
		List<Api> listaApis = new ArrayList<Api>(); 
		
		if(ControllerHelper.recuperarCache(tamPagina, pagina)){
			listaApis = Cache.getOrElse("listadoGratis", () -> Api.findApisGratisLimitada(tamPagina, pagina), 60*60*24);
		}else{
			listaApis = Api.findApisGratisLimitada(tamPagina, pagina);
		}
		
		if (ControllerHelper.soportaJson(request())) {
			res = getResultado(listaApis, totalRegistros);
		}else if (ControllerHelper.soportaXml(request())) {
			res = ok(views.xml.apis.render(listaApis,totalRegistros));
		}
		
		return res;
	}
	
	/**
	 * Action method para obtener las APIS cuya nota media de opiniones es > que la nota pasada por parametro
	 * parametro opcional: tamPagina:  numero de registros maximo a devolver. Si se manda un 0 o null se devuelven 10
	 * parametro opcional: pagina:  numero de pagina donde comenzar la devolucion de registros. Si se manda un 0 o null se devuelve la primera pagina
	 * 
	 * @param tematica tematica de la informacion que contiene la API
	 */
	public Result listadoNota(Integer notaMedia) {
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
		
		Integer totalRegistros = totalApis();
		List<Api> listaApis;
		listaApis = Api.findApisNotaLimitada(tamPagina, pagina, notaMedia);
		
		if (ControllerHelper.soportaJson(request())) {
			res = getResultado(listaApis, totalRegistros);
		}else if (ControllerHelper.soportaXml(request())) {
			res = ok(views.xml.apis.render(listaApis,totalRegistros));
		}
		
		return res;
	}
	
	/**
	 * Action method para dar de alta una API nueva en el sistema
	 * Se deben pasar los atributos de la API en el body de la peticion.  
	 * @param 
	 */
	public Result crearAPI() {
		//antes de hacer nada comprobamos si el cliente acepta nuestros formatos de respuesta
		if(!ControllerHelper.soportaFormatosAPI(request())){
			return badRequest(ControllerHelper.errorJson(1, "error.format", null));
		}		
		
		Form<Api> form = Form.form(Api.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorJson(2, "error.api", form.errorsAsJson()));
		}

		Api api = form.get();
		
		Result res = null;
		
		api.save();

		//borramos la api de cache ya que cambia su nota media y su lista de opiniones
		//borramos los listado de apis de cache
		List<String> listaCachesBorrar = new ArrayList<String>();
		listaCachesBorrar.add("listadoApis");
		listaCachesBorrar.add("listadoGratis");
		ControllerHelper.borrarCache(listaCachesBorrar);
		
		if (ControllerHelper.soportaJson(request())) {
			res = ok(Json.toJson(api));
		}		else if (ControllerHelper.soportaXml(request())) {
			res = ok(views.xml._api.render(api));
		}
		
		return res;
	}
	
	/**
	 * Action method para borrar una API del sistema
	 * @param aId id de la API
	 */
	public Result borrarAPI(Long aId) {
		//antes de hacer nada comprobamos si el cliente acepta nuestros formatos de respuesta
		if(!ControllerHelper.soportaFormatosAPI(request())){
			return badRequest(ControllerHelper.errorJson(1, "error.format", null));
		}		
		Api api = Api.find.byId(aId);
		if (api == null) {
			return notFound();
		}

		api.delete();
		
		//borramos la api de cache ya que cambia su nota media y su lista de opiniones
		//borramos los listado de apis de cache
		//borramos los listado de apis gratis de cache
		//borramos los listado de opiniones de esa api de cache
		List<String> listaCachesBorrar = new ArrayList<String>();
		listaCachesBorrar.add("api" + aId);
		listaCachesBorrar.add("listadoApis");
		listaCachesBorrar.add("listadoGratis");
		listaCachesBorrar.add("opinionesApi" + aId);
		ControllerHelper.borrarCache(listaCachesBorrar);

		return ok();
	}
	
	private Integer totalApis(){
		return Api.find.findRowCount();
	}
	
	private Result getResultado(List<Api> listaApis, Integer totalRegistros){
		Map<String, Object> resultado = new HashMap<String, Object>();
		resultado.put("totalRegistros", totalRegistros);
		resultado.put("datos", listaApis);
		return ok(Json.toJson(resultado));
	}
	
	
}
