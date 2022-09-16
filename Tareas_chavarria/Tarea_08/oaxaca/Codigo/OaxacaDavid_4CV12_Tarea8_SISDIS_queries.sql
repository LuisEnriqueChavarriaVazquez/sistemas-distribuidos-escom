drop database tarea_ocho_tienda;
create database tarea_ocho_tienda;
use tarea_ocho_tienda;
SET AUTOCOMMIT=0;

START TRANSACTION;
CREATE TABLE articulos
(
	id_articulo integer auto_increment primary key,
	nombre varchar(500) not null,
	descripcion varchar(500) not null,
	precio double not null,
	stock integer not null
);
COMMIT;

START TRANSACTION;
CREATE TABLE foto_articulos
(
	id_foto integer auto_increment primary key,
	foto longblob,
	id_articulo integer not null
);
COMMIT;

START TRANSACTION;
CREATE TABLE carrito_compra(
	id_carrito_articulo integer auto_increment primary key,
	id_articulo integer not null,
	cantidad_compra integer not null	
);

alter table foto_articulos add foreign key (id_articulo) references articulos(id_articulo);
alter table carrito_compra add foreign key (id_articulo) references articulos(id_articulo);
COMMIT;

BEGIN TRAN
INSERT INTO articulos(id_articulo, nombre, descripcion, precio, stock) VALUES (0,"From democracy","Libro de politica",320.30,100);
INSERT INTO articulos(id_articulo, nombre, descripcion, precio, stock) VALUES (0,"Manufacturing Consent","Libro de politica",230.99,205);
INSERT INTO articulos(id_articulo, nombre, descripcion, precio, stock) VALUES (0,"Sex by numbers","Libro de estadistica",433.23,23);
INSERT INTO articulos(id_articulo, nombre, descripcion, precio, stock) VALUES (0,"La sociedad del cansancio","Libro de folosofia",383.50,44);
COMMIT TRAN

BEGIN TRAN
INSERT INTO foto_articulos VALUES (0,01010101010,1);
INSERT INTO foto_articulos VALUES (0,01010101010,2);
INSERT INTO foto_articulos VALUES (0,01010101010,3);
INSERT INTO foto_articulos VALUES (0,01010101010,4);
COMMIT TRAN

BEGIN TRAN
INSERT INTO carrito_compra(id_carrito_articulo, id_articulo, cantidad_compra) VALUES (0,1,10);
INSERT INTO carrito_compra(id_carrito_articulo, id_articulo, cantidad_compra) VALUES (0,2,11);
COMMIT TRAN

-- SELECT * FROM carrito_compra;
-- SELECT * FROM foto_articulos;
-- SELECT a.id_articulo, a.nombre, a.descripcion, a.precio, a.cantidad_almacen, b.foto FROM articulos a LEFT OUTER JOIN foto_articulos b ON a.id_articulo = b.id_articulo WHERE a.descripcion LIKE '%Samsung%';

-- SELECT a.id_articulo, a.nombre, a.descripcion, a.precio, b.cantidad, c.foto FROM carrito_compra b LEFT OUTER JOIN articulos a ON a.id_articulo = b.id_articulo LEFT OUTER JOIN foto_articulos c ON b.id_articulo = c.id_articulo;
