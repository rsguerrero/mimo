package jsonobjects;


public class OpinionJson {
	
	private Long id;

	private Long idApi;
	
	private Long idUsuario;
	
	private Integer puntuacion;
	
	private boolean facilManejo;
	
	private boolean rapidezRespuesta;
	
	private String ventajas;
	
	private String inconvenientes;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdApi() {
		return idApi;
	}

	public void setIdApi(Long idApi) {
		this.idApi = idApi;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
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
