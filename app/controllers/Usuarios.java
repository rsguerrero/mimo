package controllers;

import java.util.ArrayList;
import java.util.List;

import helpers.ControllerHelper;
import models.Usuario;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Usuarios extends Controller{
	
	
	/**
	 * Action method para dar de alta un usuario nuevo en el sistema 
	 * Se deben pasar los atributos del usuario en el body de la peticion. 
	 */
	public Result crearUsuario() {
		//antes de hacer nada comprobamos si el cliente acepta nuestros formatos de respuesta
		if(!ControllerHelper.soportaFormatosAPI(request())){
			return badRequest(ControllerHelper.errorJson(1, "error.format", null));
		}		
		
		Form<Usuario> form = Form.form(Usuario.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorJson(2, "error.usuario", form.errorsAsJson()));
		}

		Usuario usuarioNuevo = form.get();
	
		Usuario usuario2 = Usuario.findUsuarioByNick(usuarioNuevo.getNick());
		if (usuario2 != null) {
			return badRequest(ControllerHelper.errorJson(1, "error.nick.existente", null));
		}

		Result res = null;


		usuarioNuevo.save();
		
		if (ControllerHelper.soportaJson(request())) {
			res = ok(Json.toJson(usuarioNuevo));
		}		else if (ControllerHelper.soportaXml(request())) {
			res = ok(views.xml._usuario.render(usuarioNuevo));
		}
		
		return res;
	}
	
	/**
	 * Action method para borrar un usuario del sistema
	 * Se deben pasar los atributos del usuario en el body de la peticion. 
	 */
	public Result borrarUsuario(Long uId) {
		//antes de hacer nada comprobamos si el cliente acepta nuestros formatos de respuesta
		if(!ControllerHelper.soportaFormatosAPI(request())){
			return badRequest(ControllerHelper.errorJson(1, "error.format", null));
		}		
		
		String password = request().getQueryString("pass") == null? "" : request().getQueryString("pass");
		Usuario usuario = Usuario.find.byId(uId);
		if (usuario == null) {
			return notFound();
		}
		
		
		if(usuario.getPassword().equals(password)){
			//borramos los listado de opiniones de ese usuario de cache
			List<String> listaCachesBorrar = new ArrayList<String>();
			listaCachesBorrar.add("opinionesUsuario" + usuario.id);
			ControllerHelper.borrarCache(listaCachesBorrar);
			usuario.delete();
			return ok();
		}else{
			return badRequest(ControllerHelper.errorJson(2, "error.password.incorrecta", null));
		}
	}
}
