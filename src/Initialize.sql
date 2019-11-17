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
  FOREIGN KEY(vtname) REFERENCES VehicleType
);


CREATE TABLE Customer(
	cellphone NUMBER PRIMARY KEY,
  name VARCHAR2(20),
  address VARCHAR2(20),
  dlicense VARCHAR2(20)
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
);

CREATE TABLE Rental (
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
  FOREIGN KEY (vid) REFERENCES Vehicle,
  FOREIGN KEY (cellphone) REFERENCES Customer,
  FOREIGN KEY (confNo) REFERENCES Reservation
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