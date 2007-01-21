create table AUTO_PK_SUPPORT (TABLE_NAME CHAR(100) NOT NULL,  NEXT_ID INTEGER NOT NULL, PRIMARY KEY(TABLE_NAME));
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('FieldPersistenceEntity', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('PropertyPersistenceEntity', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('TransientFieldsEntity', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('CollectionFieldPersistenceEntity', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('MapFieldPersistenceEntity', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('HelperEntity1', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('HelperEntity2', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('HelperEntity3', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('HelperEntity4', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('EmbeddedEntity', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('EmbeddedImpliedEntity', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('PropertyDefaultsPrimitiveEntity', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('PropertyDefaultsWrapperEntity', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('PropertyDefaultsDatesEntity', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('PropertyDefaultsOtherEntity', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('BidiOneToOneOwner', 1);
insert into AUTO_PK_SUPPORT (TABLE_NAME, NEXT_ID) VALUES ('BidiOneToOneOwned', 1);

create table FieldPersistenceEntity (id int not null, property1 VARCHAR(100), primary key(id));
create table PropertyPersistenceEntity (id int not null, property1 VARCHAR(100), property2 BOOLEAN, primary key(id));
create table TransientFieldsEntity (id int not null, property1 VARCHAR(100), primary key(id));
create table CollectionFieldPersistenceEntity (id int not null, primary key(id));
create table MapFieldPersistenceEntity (id int not null, primary key(id));
create table HelperEntity1 (id int not null, entity_id int, primary key(id));
create table HelperEntity2 (id int not null, entity_id int, primary key(id));
create table HelperEntity3 (id int not null, entity_id int, primary key(id));
create table HelperEntity4 (id int not null, entity_id int, primary key(id));
create table NoPkEntity (property1 VARCHAR(100));
create table IdClassEntity (id1 int not null, id2 int not null, property1 VARCHAR(100), primary key(id1, id2));
create table EmbeddedIdEntity (id1 int not null, id2 int not null, property1 VARCHAR(100), primary key(id1, id2));
create table EmbeddedEntity (id int not null, property1 VARCHAR(100), primary key(id));
create table EmbeddedImpliedEntity (id int not null, property1 VARCHAR(100), primary key(id));
create table PropertyDefaultsPrimitiveEntity (id INT not null, primitiveBoolean BOOLEAN, primitiveByte TINYINT, primitiveShort SMALLINT, primitiveChar CHAR(1), primitiveInt INT, primitiveLong BIGINT, primitiveFloat FLOAT, primitiveDouble DOUBLE, primary key(id));
create table PropertyDefaultsWrapperEntity (id INT not null,  booleanWrapper BOOLEAN, byteWrapper TINYINT, shortWrapper SMALLINT, intWrapper INT, charWrapper CHAR(1), longWrapper BIGINT, floatWrapper FLOAT, doubleWrapper DOUBLE, primary key(id));
create table PropertyDefaultsDatesEntity (id int not null, utilDate TIMESTAMP, calendar TIMESTAMP, sqlDate DATE, sqlTime TIME, sqlTimestamp TIMESTAMP, primary key(id));
create table PropertyDefaultsOtherEntity (id int not null, string VARCHAR(100), bigInt BIGINT, bigDecimal NUMERIC, byteArray VARBINARY(100), byteWrapperArray VARBINARY(100), charArray VARCHAR(100), charWrapperArray VARCHAR(100), enumType INT, serializableType VARBINARY(200), primary key(id));
create table BidiOneToOneOwned (id int not null, primary key(id));
create table BidiOneToOneOwner (id int not null, owned_id int not null, primary key(id));
alter table BidiOneToOneOwner add constraint cfk_owned_id FOREIGN KEY (owned_id) references BidiOneToOneOwned (id);
alter table BidiOneToOneOwner add constraint cu_owned_id UNIQUE (owned_id);
