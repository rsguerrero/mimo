package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

@Entity
public class Usuario extends ApiModeloBase {
	
	@Required(message="error.obligatorio.nick")
	private String nick;
	
	@Required(message="error.obligatorio.password")
	@MinLength(value=8,message="error.password.min")
	@MaxLength(value=12,message="error.password.max")
	private String password;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="usuario")
	private List<Opinion> opiniones = new ArrayList<Opinion>();
	
	public static final Find<Long, Usuario> find = new Find<Long, Usuario>(){};
	
	public static Usuario findUsuarioById(Long id) {
		return find.byId(id);
	}
	
	public static Usuario findUsuarioByNick(String nick) {
		return find.where().eq("nick", nick).findUnique();
	}
	
	public List<Opinion> getOpinionesPaginado(Integer tamPagina, Integer pagina) {
		List<Opinion> opiniones = this.getOpiniones();
		//comprobamos los limites de la lista
		Integer comienzo = (pagina*tamPagina) > opiniones.size() ? 0 : pagina*tamPagina;
		Integer fin = (comienzo + tamPagina) > opiniones.size() ? opiniones.size() : comienzo + tamPagina;
		return opiniones.subList(comienzo, fin);
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public List<Opinion> getOpiniones() {
		return opiniones;
	}

	public void setOpiniones(List<Opinion> opiniones) {
		this.opiniones = opiniones;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	

}
