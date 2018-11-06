package ftn.kts.transport.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="ZONES")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Zone implements Ticketable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String name;
	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Station> stations;
	
	public Zone() {
		
	}

	public Zone(String name, Set<Station> stations) {
		super();
		this.name = name;
		this.stations = stations;
	}

	public Zone(Long id, String name, Set<Station> stations) {
		super();
		this.id = id;
		this.name = name;
		this.stations = stations;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Station> getStations() {
		return stations;
	}

	public void setStations(Set<Station> stations) {
		this.stations = stations;
	}
	
	
	
	
}
