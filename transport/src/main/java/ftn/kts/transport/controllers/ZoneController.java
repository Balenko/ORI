package ftn.kts.transport.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ftn.kts.transport.dtos.StationDTO;
import ftn.kts.transport.dtos.ZoneDTO;
import ftn.kts.transport.exception.StationNotFoundException;
import ftn.kts.transport.exception.ZoneNotFoundException;
import ftn.kts.transport.model.Station;
import ftn.kts.transport.model.Zone;
import ftn.kts.transport.services.StationService;
import ftn.kts.transport.services.ZoneService;

@RestController
@RequestMapping(value = "/zone")
public class ZoneController {
	
	@Autowired
	private ZoneService zoneService;
	@Autowired
	private StationService stationService;
	
	@GetMapping("/all")
	@Produces("application/json")
	public ResponseEntity<List<ZoneDTO>> getAllZones() {
		List<Zone> zones = zoneService.findAll();
		List<ZoneDTO> dtoZones = new ArrayList<ZoneDTO>();
		for(Zone z : zones) {
			ZoneDTO dto = new ZoneDTO(z);
			dtoZones.add(dto);
		}
		return new ResponseEntity<>(dtoZones, HttpStatus.OK);
	}
	
	@PostMapping(path="/add")
	@Consumes("application/json")
	public ResponseEntity<ZoneDTO> addZone(@RequestBody ZoneDTO zoneDTO) {
		
		if(zoneDTO.getSubZoneId() == null || zoneDTO.getStations().size() == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Zone subZone = null;
		if(zoneDTO.getSubZoneId() != 0) {
			subZone = zoneService.findById(zoneDTO.getSubZoneId());
		}
		
		Set<Station> stations = new HashSet<Station>();
		for(StationDTO s : zoneDTO.getStations()) {
			Station station = stationService.findById(s.getId());
			stations.add(station);
		}
		Zone zone = zoneService.save(new Zone(zoneDTO.getName(), stations, subZone, true));
		
		return new ResponseEntity<>(new ZoneDTO(zone), HttpStatus.CREATED);	
	}
	
	@DeleteMapping(path="/delete/{id}")
	public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
		zoneService.deleteZone(id);	
		
		return new ResponseEntity<>(HttpStatus.OK);	
	}
	
	@PostMapping(path="/addStations/{id}") //id zone
	@Consumes("applications/json")
	public ResponseEntity<Void> addStationsInZone(@PathVariable Long id, @RequestBody List<StationDTO> dtoStations){
		
		Set<Station> stations = checkStations(new HashSet<StationDTO>(dtoStations));
				
		Zone zone = zoneService.findById(id);
		zone.setStations(stations);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping(path="/update")
	@Consumes("applications/json")
	@Produces("applications/json")
	public ResponseEntity<ZoneDTO> updateZone(@RequestBody ZoneDTO dtoZone){
		
		if(dtoZone.getSubZoneId() == null || dtoZone.getStations().size() == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Zone zone = zoneService.findById(dtoZone.getId());
		
		zone.setName(dtoZone.getName());
		
		Zone subZone = zoneService.findById(dtoZone.getSubZoneId());
		zone.setSubZone(subZone);
		
		Set<Station> stations = checkStations(dtoZone.getStations());
		zone.setStations(stations);
		
		zoneService.save(zone);
		return new ResponseEntity<>(new ZoneDTO(zone), HttpStatus.OK);
	}
	
	public Set<Station> checkStations(Set<StationDTO> dtoStations){
		Set<Station> stations = new HashSet<Station>();
		
		for(StationDTO dtoStation : dtoStations) {
			Station station = stationService.fromDtoToStation(dtoStation);
			if(station == null)
				throw new StationNotFoundException(dtoStation.getId());
			stations.add(station);
		}
		return stations;
		
	}
	
}
