create table cats3
( id int not null auto_increment, 
   name varchar(20), 
   breed varchar(20),
   age int,
   primary key (id)
);

insert into cats3
(name, breed, age)
values
('Ringo', 'Tabby', 4),
('Cindy', 'Coon', 10),
('Dumbledore', 'Coon', 11),
('Egg', 'Persian', 4),
('Misty', 'Tabby', 13),
('George', 'Ragdoll', 9),
('Jackson', 'Sphynx', 7);



