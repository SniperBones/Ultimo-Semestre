CREATE TABLE Articulos(
	ID_articulo int auto_increment primary key,
    Nombre varchar(255) not null,
    Descripcion varchar(255) not null,
    Precio float(10,2) not null,
    Cantidad int not null,
    Foto longblob not null
);

CREATE TABLE Carrito(
	ID_articulo int not null,
    Cantidad int not null,
    foreign key (ID_articulo) references Articulos(ID_articulo)
);

DELIMITER $$
CREATE DEFINER=`Alejandro`@`localhost` PROCEDURE `borrarArticulo`(IDf int,Cantidadf int)
BEGIN
		DECLARE auxCantidad int;
	DECLARE track_no INT DEFAULT 0;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION, NOT FOUND, SQLWARNING
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 @`errno` = MYSQL_ERRNO, @`sqlstate` = RETURNED_SQLSTATE, @`text` = MESSAGE_TEXT;
        SET @full_error = CONCAT('ERROR ', @`errno`, ' (', @`sqlstate`, '): ', @`text`);
        SET track_no = 0;
        SELECT track_no, @full_error;
    END;
    START TRANSACTION;
		SELECT Cantidad INTO auxCantidad FROM Articulos WHERE ID_articulo=IDf;
		UPDATE Articulos SET Cantidad = (auxCantidad+Cantidadf) WHERE ID_articulo=IDf;
        DELETE FROM Carrito WHERE ID_articulo=IDf;
        SET track_no = 1;
        SELECT track_no,"Articulo Eliminado";
	COMMIT;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`Alejandro`@`localhost` PROCEDURE `Compra`(IDf int,Cantidadf int)
BEGIN
	DECLARE auxCantidad int;
	DECLARE track_no INT DEFAULT 0;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION, NOT FOUND, SQLWARNING
    BEGIN
        ROLLBACK;
        GET DIAGNOSTICS CONDITION 1 @`errno` = MYSQL_ERRNO, @`sqlstate` = RETURNED_SQLSTATE, @`text` = MESSAGE_TEXT;
        SET @full_error = CONCAT('ERROR ', @`errno`, ' (', @`sqlstate`, '): ', @`text`);
        SELECT track_no, @full_error;
    END;
    START TRANSACTION;
		SELECT Cantidad INTO auxCantidad FROM Articulos WHERE ID_articulo=IDf;
        SET track_no = 1;
		IF Cantidadf <= auxCantidad THEN
			INSERT INTO Carrito(ID_articulo,Cantidad) VALUES(IDf,Cantidadf);
            SET track_no = 2;
			UPDATE Articulos SET Cantidad = (auxCantidad-Cantidadf) WHERE ID_articulo=IDf;
            SET track_no = 3;
			SELECT track_no, 'OK';
		ELSE
            SET @full_error = "No se cumple la cantidad de articulos";
			SELECT track_no, @full_error;
		END IF;
	COMMIT;
END$$
DELIMITER ;
