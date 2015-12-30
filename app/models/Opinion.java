package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model.Find;
import com.fasterxml.jackson.annotation.JsonIgnore;

import play.data.validation.Constraints.Max;
import play.data.validation.Constraints.Min;
import play.data.validation.Constraints.Required;

@Entity
public class Opinion extends ApiModeloBase {
	
	@ManyToOne
	@Required(message="error.obligatorio.api")
	@JsonIgnore
	private Api api;
	
	@ManyToOne
	@Required(message="error.obligatorio.usuario")
	@JsonIgnore
	private Usuario usuario;
	
	@Required(message="error.obligatorio.puntuacion")
	@Min(value = 0,message="error.puntuacion.min")
	@Max(value = 10,message="error.puntuacion.max")
	private Integer puntuacion;
	
	@Required(message="error.obligatorio.facilManejo")
	private boolean facilManejo;
	
	@Required(message="error.obligatorio.rapidezRespuesta")
	private boolean rapidezRespuesta;
	
	private String ventajas;
	
	private String inconvenientes;
	
	public static final Find<Long, Opinion> find = new Find<Long, Opinion>(){};
	
	public static Opinion findOpinion(Long id) {
		return find.byId(id);
	}

	public Api getApi() {
		return api;
	}

	public void setApi(Api api) {
		this.api = api;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(Integer puntuacion) {
		this.puntuacion = puntuacion;
	}

	public boolean isFacilManejo() {
		return facilManejo;
	}

	public void setFacilManejo(boolean facilManejo) {
		this.facilManejo = facilManejo;
	}

	public boolean isRapidezRespuesta() {
		return rapidezRespuesta;
	}

	public void setRapidezRespuesta(boolean rapidezRespuesta) {
		this.rapidezRespuesta = rapidezRespuesta;
	}

	public String getVentajas() {
		return ventajas;
	}

	public void setVentajas(String ventajas) {
		this.ventajas = ventajas;
	}

	public String getInconvenientes() {
		return inconvenientes;
	}

	public void setInconvenientes(String inconvenientes) {
		this.inconvenientes = inconvenientes;
	}

	
	
	

}
