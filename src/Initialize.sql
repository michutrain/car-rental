CREATE TABLE VehicleType(
  vtname    VARCHAR2(20) PRIMARY KEY,
  features  VARCHAR2(50),
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
  vlicense  VARCHAR2(20),
  make      VARCHAR2(20),
  model     VARCHAR2(20),
  year      NUMBER,
  color     VARCHAR2(20),
  odometer  NUMBER,
  status    NUMBER,
  vtname    VARCHAR2(20) NOT NULL,
  branch    VARCHAR2(50),
  FOREIGN KEY(vtname) REFERENCES VehicleType
);


CREATE TABLE Customer(
  dlicense  VARCHAR2(20) PRIMARY KEY,
  cellphone VARCHAR2(20),
  name      VARCHAR2(20),
  address   VARCHAR2(40)
);

CREATE TABLE Reservation(
  confNo			  NUMBER PRIMARY KEY,
  vtname			  VARCHAR2(20) NOT NULL,
  dlicense		  VARCHAR2(20) NOT NULL,
  fromTimestamp	TIMESTAMP,
  toTimestamp   TIMESTAMP,
  FOREIGN KEY (vtname) REFERENCES VehicleType,
  FOREIGN KEY (dlicense) REFERENCES Customer,
  CHECK(fromTimestamp < toTimestamp)
);

CREATE TABLE Rental (
  rid 				  NUMBER PRIMARY KEY,
  vid 				  NUMBER NOT NULL,
  dlicense 	    VARCHAR2(20) NOT NULL,
  fromTimestamp	TIMESTAMP,
  toTimestamp	  TIMESTAMP,
  odometer		  NUMBER,
  FOREIGN KEY (vid) REFERENCES Vehicle,
  FOREIGN KEY (dlicense) REFERENCES Customer,
  CHECK(fromTimestamp < toTimestamp)
);


CREATE TABLE Return(
  rid       NUMBER PRIMARY KEY,
  vid				NUMBER NOT NULL,
  stamp     TIMESTAMP,
  value     NUMBER,
  FOREIGN KEY(rid) REFERENCES Rental,
  FOREIGN KEY(vid) REFERENCES Vehicle
);