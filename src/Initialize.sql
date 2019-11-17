CREATE TABLE TimePeriod(
  fromDate DATE,
  fromTime interval day (0) to second(0),
  toDate DATE,
  toTime interval day (0) to second(0),
  PRIMARY KEY(fromDate, fromTime, toDate, toTime)
);

CREATE TABLE Branch(
  location  VARCHAR2(20),
  city      VARCHAR2(20),
  PRIMARY KEY (location, city)
);

CREATE TABLE VehicleType(
  vtname    VARCHAR2(20) PRIMARY KEY,
  features  VARCHAR2(20),
  wrate     NUMBER,
  drate     NUMBER,
  hrate     NUMBER,
  wirate    NUMBER,
  dirate    NUMBER,
  hirate    NUMBER,
  krate     NUMBER
);

CREATE TABLE Vehicle(
  vid       NUMBER PRIMARY KEY,
  vlicense  CHAR(6),
  make      VARCHAR2(20),
  model     VARCHAR2(20),
  year      NUMBER,
  color     VARCHAR2(20),
  odometer  NUMBER,
  status    CHAR(8),
  vtname    VARCHAR2(20),
  location  VARCHAR2(20),
  city      VARCHAR2(20),
  FOREIGN KEY(location, city) REFERENCES Branch ON DELETE SET NULL,
  FOREIGN KEY(vtname) REFERENCES VehicleType
);

CREATE TABLE ForSale(
  vid       NUMBER PRIMARY KEY,
  vlicense  CHAR(6),
  make      VARCHAR2(20),
  model     VARCHAR2(20),
  year      NUMBER,
  color     VARCHAR2(20),
  odometer  NUMBER,
  status    CHAR(8),
  vtname    VARCHAR2(20),
  location  VARCHAR2(20),
  city      VARCHAR2(20),
  FOREIGN KEY(location, city) REFERENCES Branch ON DELETE SET NULL,
  FOREIGN KEY(vtname) REFERENCES VehicleType
);

CREATE TABLE ForRent(
  vid       NUMBER PRIMARY KEY,
  vlicense  CHAR(6),
  make      VARCHAR2(20),
  model     VARCHAR2(20),
  year      NUMBER,
  color     VARCHAR2(20),
  odometer  NUMBER,
  status    CHAR(8),
  vtname    VARCHAR2(20),
  location  VARCHAR2(20),
  city      VARCHAR2(20),
  FOREIGN KEY(location, city) REFERENCES Branch ON DELETE SET NULL,
  FOREIGN KEY(vtname) REFERENCES VehicleType
);

CREATE TABLE EquipType(
	etname VARCHAR2(20) PRIMARY KEY,
  drate NUMBER,
  hrate NUMBER
);

CREATE TABLE Equipment(
	eid NUMBER PRIMARY KEY,
  etname 	VARCHAR2(20),
  status 	VARCHAR2(20),
  location VARCHAR2(20),
  city 		VARCHAR2(20),
  FOREIGN KEY(location, city) REFERENCES Branch,
  FOREIGN KEY(etname) REFERENCES EquipType
);

CREATE TABLE Is_for(
	etname 	VARCHAR2(20),
  vtname 	VARCHAR2(20),
  PRIMARY KEY(etname, vtname),
  FOREIGN KEY(etname) REFERENCES EquipType,
  FOREIGN KEY(vtname) REFERENCES VehicleType
);

CREATE TABLE Customer(
	cellphone NUMBER PRIMARY KEY,
  name VARCHAR2(20),
  address VARCHAR2(20),
  dlicense VARCHAR2(20)
);

CREATE TABLE ClubMember(
	cellphone NUMBER PRIMARY KEY,
  points NUMBER,
  FEES NUMBER,
  FOREIGN KEY(cellphone) REFERENCES Customer
);

CREATE TABLE Reservation(
	confNo			NUMBER PRIMARY KEY,
  vtname			VARCHAR2(20),
  cellphone		NUMBER,
  fromDate		DATE,
  fromTime 		interval day (0) to second(0),
  toDate			DATE,
  toTime			interval day (0) to second(0),
  FOREIGN KEY (vtname) REFERENCES VehicleType,
  FOREIGN KEY (cellphone) REFERENCES Customer,
  FOREIGN KEY (fromDate, fromTime, toDate, toTime) REFERENCES TimePeriod
);

CREATE TABLE Reserve_Includes (
	confNo NUMBER,
  etname VARCHAR2(20),
  PRIMARY KEY(confNo, etname),
  FOREIGN KEY(confNo) REFERENCES Reservation,
  FOREIGN KEY(etname) REFERENCES EquipType
);

CREATE TABLE Rent (
	rid 				NUMBER PRIMARY KEY,
  vid 				NUMBER NOT NULL,
  cellphone 	NUMBER NOT NULL,
  fromDate		DATE NOT NULL,
  fromTime 		interval day (0) to second(0) NOT NULL,
  toDate			DATE NOT NULL,
  toTime			interval day (0) to second(0) NOT NULL,
  odometer		NUMBER,
  cardName		VARCHAR2(20),
  cardNo			VARCHAR2(20),
  ExpDate			DATE,
  confNo			NUMBER,
  FOREIGN KEY (vid) REFERENCES ForRent,
  FOREIGN KEY (cellphone) REFERENCES Customer,
  FOREIGN KEY (fromDate, fromTime, toDate, toTime) REFERENCES TimePeriod,
  FOREIGN KEY (confNo) REFERENCES Reservation
);


CREATE TABLE RentIncludes (
	rid NUMBER,
  eid NUMBER,
  PRIMARY KEY (rid, eid),
  FOREIGN KEY (rid) REFERENCES Rent,
  FOREIGN KEY (eid) REFERENCES Equipment
);

CREATE TABLE Return(
	rid NUMBER PRIMARY KEY,
  "date" DATE,
  time DATE,
  odometer NUMBER,
  fulltank NUMBER,
  value NUMBER,
  FOREIGN KEY(rid) REFERENCES Rent
);