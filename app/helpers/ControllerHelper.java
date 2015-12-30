package helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jsonobjects.OpinionJson;
import models.Opinion;
import play.cache.Cache;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Http.Request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ControllerHelper {

	public static JsonNode errorJson(Integer code, String message, JsonNode errors) {
		ObjectNode node = Json.newObject();
		node.put("code", code);
		node.put("message", Messages.get(message));
		node.putPOJO("errors", errors);
		return node;
	}
	
	public static boolean soportaFormatosAPI(Request request){
		return (request.accepts("application/xml") || request.accepts("text/xml") || request.accepts("application/json"));
	}
	
	public static boolean soportaJson(Request request) {
		return request.accepts("application/json");
	}
	
	public static boolean soportaXml(Request request) {
		return (request.accepts("application/xml") || request.accepts("text/xml"));
	}
	
	//solo buscamos en la cache si no estamos paginando mas alla de la primera página.
	//podria meter en cache dinamicamente concatenando la pagina y el tamaño pero no he visto como borrar luego de la cache todo lo que haya con algun patron en el nombre
	public static boolean recuperarCache(Integer tamPagina, Integer pagina){
		return (tamPagina == 10 && pagina == 0);
	}
	
	
	public static Integer getTamPagina(String queryString){
		try{
			return queryString == null ? 10 : Integer.valueOf(queryString);
		}catch(Exception e){//si lo que no pasan no es un número devolvemos un error informando
			return null;
		}
	}
	
	public static Integer getPagina(String queryString){		
		try{
			return queryString == null ? 0 : Integer.valueOf(queryString);
		}catch(Exception e){//si lo que nos pasan no es un número devolvemos un error informando
			return null;
		}
	}
	
	//Metodo que recibe una lista con la caches que debe borrar
	public static void borrarCache(List<String> listaEliminar){
		ListIterator<String> iterator = listaEliminar.listIterator();
		while (iterator.hasNext()){
			Cache.remove(iterator.next());
		}
	}
	
	//este metodo lo utilizamos para poder devolver el id de la api y del usuario en la respuesta json
	public static OpinionJson getOpinionJson(Opinion opinion){
		OpinionJson result = new OpinionJson();
		result.setId(opinion.id);
		result.setIdApi(opinion.getApi().id);
		result.setIdUsuario(opinion.getUsuario().id);
		result.setFacilManejo(opinion.isFacilManejo());
		result.setRapidezRespuesta(opinion.isRapidezRespuesta());
		result.setPuntuacion(opinion.getPuntuacion());
		result.setVentajas(opinion.getVentajas());
		result.setInconvenientes(opinion.getInconvenientes());
		
		return result;
	}
	
	//este metodo lo utilizamos para poder devolver el id de la api y del usuario en la respuesta json
	public static List<OpinionJson> getListOpinionJson(List<Opinion> opiniones){
		List<OpinionJson> opinionesJson = new ArrayList<OpinionJson>();
		//comprobamos los limites de la lista
		Iterator<Opinion> iterator = opiniones.iterator();
		while (iterator.hasNext()){
			Opinion opinion = iterator.next();
			opinionesJson.add(ControllerHelper.getOpinionJson(opinion));
		}
		
		return opinionesJson;
	}
	
}
