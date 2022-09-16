drop database if exists tienda;
create database tienda;
use tienda;

create table articulos(
    IDArticulo int auto_increment not null,
    Nombre_articulo varchar(50),
    Descripcion_articulo varchar(100),
    Precio_precio float,
    Cantidad_articulo int,
    Foto_articulo longblob,
    primary key (IDArticulo)
);

create table carro(
    IDCompra int not null auto_increment,
    IDArticulo int,
    Cantidad_carro int,
    primary key (IDCompra),
    foreign key (IDArticulo) references articulos(IDArticulo)
);

