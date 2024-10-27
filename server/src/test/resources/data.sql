insert into users ("name", "email")
values('ivan', 'ivan@mail.ru');
insert into users ("name", "email")
values('john', 'john@mail.ru');
insert into users ("name", "email")
values('ira', 'ira@mail.ru');

insert into items("name","description","available","owner_id")
values('item1','desc1','true','1');
insert into items("name","description","available","owner_id")
values('item2','desc2','false','2');

insert into bookings("start_time","end_time", "status", "booker_id","item_id")
values('2011-01-01 00:00:00', '2011-02-01 00:00:00', 'APPROVED', '3','2');
insert into bookings("start_time","end_time", "status", "booker_id","item_id")
values('2012-01-01 00:00:00', '2013-02-01 00:00:00', 'APPROVED', '3','1');


insert into requests("owner_id", "description", "created")
values('3','need nice item','2023-01-01 00:00:00');
insert into requests("owner_id", "description", "created")
values('3','need more items','2023-02-01 00:00:00');