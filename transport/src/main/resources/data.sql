insert into kts_lines(id, active, name, transport_type) values (1, 1, '7A', 0)
insert into kts_lines(id, active, name, transport_type) values (2, 1, '4A', 0)

insert into stations(id, active, address, name) values (1, 1, 'Bulevar Oslobodjenja 14', 'Bul. Oslobodjenja - Lutrija')
insert into stations(id, active, address, name) values (2, 1, 'Bulevar Oslobodjenja 50', 'Aleksandar zgrada')

insert into line_and_station(line_id, station_id, station_order) values (1, 1, 1)
insert into line_and_station(line_id, station_id, station_order) values (1, 2, 2)