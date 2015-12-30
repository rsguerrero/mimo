package models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import play.data.validation.Constraints.Required;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class ApiModeloBase extends Model {
	
	@Id
	@GeneratedValue
	public Long id;
	
	@Version
	Long version;
	
    @CreatedTimestamp
    Timestamp fechaCreacion;
    
    @UpdatedTimestamp
    Timestamp fechaModicacion;

}
