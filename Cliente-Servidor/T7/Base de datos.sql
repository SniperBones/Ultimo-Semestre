CREATE TABLE Articulos(
	ID_articulo int auto_increment primary key,
    Nombre varchar(256) not null,
    Descripcion varchar(256) not null,
    Precio decimal(10,2) not null,
    Cantidad int not null,
    Foto longblob not null
);

CREATE TABLE Carrito(
	ID_articulo int not null,
    Cantidad int not null,
    foreign key (ID_articulo) references Articulos(ID_articulo)
);