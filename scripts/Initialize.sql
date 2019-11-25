CREATE TABLE Vehicle(
  vid       NUMBER PRIMARY KEY,
  vlicense  VARCHAR2(20),
  make      VARCHAR2(20),
  model     VARCHAR2(20),
  year      NUMBER,
  color     VARCHAR2(20),
  odometer  NUMBER,
  status    NUMBER,
  vtname    VARCHAR2(20) NOT NULL,
  branch    VARCHAR2(50)
);


CREATE TABLE Customer(
  dlicense  VARCHAR2(20) PRIMARY KEY,
  cellphone VARCHAR2(20),
  name      VARCHAR2(20),
  address   VARCHAR2(40)
);

CREATE TABLE Reservation(
  confNo		NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
  vtname		VARCHAR2(20) NOT NULL,
  dlicense		VARCHAR2(20) NOT NULL,
  fromTimestamp	TIMESTAMP,
  toTimestamp   TIMESTAMP,
  FOREIGN KEY (dlicense) REFERENCES Customer,
  CHECK(fromTimestamp < toTimestamp)
);

CREATE TABLE Rental (
  rid 			NUMBER PRIMARY KEY,
  vid 			NUMBER NOT NULL,
  dlicense 	    VARCHAR2(20) NOT NULL,
  fromTimestamp	TIMESTAMP,
  toTimestamp	TIMESTAMP,
  odometer		NUMBER,
  FOREIGN KEY (vid) REFERENCES Vehicle,
  FOREIGN KEY (dlicense) REFERENCES Customer,
  CHECK(fromTimestamp < toTimestamp)
);


CREATE TABLE Return(
  rid       NUMBER PRIMARY KEY,
  vid		NUMBER NOT NULL,
  stamp     TIMESTAMP,
  value     NUMBER,
  FOREIGN KEY(rid) REFERENCES Rental,
  FOREIGN KEY(vid) REFERENCES Vehicle
);