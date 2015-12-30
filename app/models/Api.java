package models;

import helpers.ControllerHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import jsonobjects.OpinionJson;

import org.hibernate.validator.constraints.URL;

import play.data.validation.Constraints.Max;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Min;
import play.data.validation.Constraints.Required;

@Entity
public class Api extends ApiModeloBase {
	
	@Required(message="error.obligatorio.descripcion")
	@MaxLength(value=100,message="error.descripcion.max")
	private String descripcion;
	
	@Required(message="error.obligatorio.tematica")
	@MaxLength(value=30,message="error.tematica.max")
	private String tematica;
	
	@Required(message="error.obligatorio.url")
	@URL(message="error.formato.url")
	private String url;
	
	@Required(message="error.obligatorio.gratis")
	private boolean gratis;
	
	@Required(message="error.obligatorio.precioMes")
	@Min(value = 0,message="error.precioMes.min")
	private Integer precioMes;
	
	private BigDecimal puntuacionMedia;
	
	@Required(message="error.obligatorio.xml")
	private boolean xml;
	
	@Required(message="error.obligatorio.json")
	private boolean json;
	
	@Required(message="error.obligatorio.html")
	private boolean html;
	
	@Required(message="error.obligatorio.text")
	private boolean text;
	
	@Required(message="error.obligatorio.bytes")
	private boolean bytes;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="api")
	private List<Opinion> opiniones = new ArrayList<Opinion>();
	
	public static final Find<Long, Api> find = new Find<Long, Api>(){};
	
	public static Api findApi(Long id) {
		return find.byId(id);
	}
	
	public static List<Api> findApis() {
		return find.orderBy("id").findList();
	}
	
	public static List<Api> findApisLimitado(Integer tamañoPagina, Integer pagina) {
		return find.orderBy("id").setMaxRows(tamañoPagina).setFirstRow(pagina*tamañoPagina).findList();
	}
	
	public static List<Api> findApisTematicaLimitada(Integer tamPagina, Integer pagina, String tematica) {
		return find.where().eq("tematica", tematica).orderBy("id").setMaxRows(tamPagina).setFirstRow(pagina*tamPagina).findList();
	}
	
	public static List<Api> findApisGratisLimitada(Integer tamPagina, Integer pagina) {
		return find.where().eq("gratis", true).orderBy("id").setMaxRows(tamPagina).setFirstRow(pagina*tamPagina).findList();
	}
	
	public static List<Api> findApisFormatoLimitada(Integer tamPagina, Integer pagina, String formatoRespuesta) {
		return find.where().eq(formatoRespuesta, true).orderBy("id").setMaxRows(tamPagina).setFirstRow(pagina*tamPagina).findList();
	}
	
	public static List<Api> findApisNotaLimitada(Integer tamPagina, Integer pagina, Integer nota) {
		return find.where().gt("puntuacionMedia", nota).orderBy("id").setMaxRows(tamPagina).setFirstRow(pagina*tamPagina).findList();
	}
	
	
	public List<Opinion> getOpinionesPaginado(Integer tamPagina, Integer pagina) {
		List<Opinion> opiniones = this.getOpiniones();
		//comprobamos los limites de la lista
		Integer comienzo = (pagina*tamPagina) > opiniones.size() ? 0 : pagina*tamPagina;
		Integer fin = (comienzo + tamPagina) > opiniones.size() ? opiniones.size() : comienzo + tamPagina;
		
		return opiniones.subList(comienzo, fin);
	}
	
	public static boolean comprobarFormatos(String formato){
		switch (formato){
		case "xml":
			return true;
		case "json":
			return true;
		case "html":
			return true;
		case "text":
			return true;
		case "bytes":
			return true;
		default:
			return false;
		}
	}


	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTematica() {
		return tematica;
	}

	public void setTematica(String tematica) {
		this.tematica = tematica;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public boolean isGratis() {
		return gratis;
	}

	public void setGratis(boolean gratis) {
		this.gratis = gratis;
	}

	public Integer getPrecioMes() {
		return precioMes;
	}

	public void setPrecioMes(Integer precioMes) {
		this.precioMes = precioMes;
	}

	public BigDecimal getPuntuacionMedia() {
		return puntuacionMedia;
	}

	public void setPuntuacionMedia(BigDecimal puntuacionMedia) {
		this.puntuacionMedia = puntuacionMedia;
	}

	public List<Opinion> getOpiniones() {
		return opiniones;
	}

	public void setOpiniones(List<Opinion> opiniones) {
		this.opiniones = opiniones;
	}

	public boolean isXml() {
		return xml;
	}

	public void setXml(boolean xml) {
		this.xml = xml;
	}

	public boolean isJson() {
		return json;
	}

	public void setJson(boolean json) {
		this.json = json;
	}

	public boolean isHtml() {
		return html;
	}

	public void setHtml(boolean html) {
		this.html = html;
	}

	public boolean isText() {
		return text;
	}

	public void setText(boolean text) {
		this.text = text;
	}

	public boolean isBytes() {
		return bytes;
	}

	public void setBytes(boolean bytes) {
		this.bytes = bytes;
	}
	
	
	
	

}
