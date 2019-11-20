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
  vlicense  CHAR(6) PRIMARY KEY,
  vid       NUMBER,
  make      VARCHAR2(20),
  model     VARCHAR2(20),
  year      NUMBER,
  color     VARCHAR2(20),
  odometer  NUMBER,
  status    NUMBER,
  vtname    VARCHAR2(20) NOT NULL,
  branch    VARCHAR2(20),
  FOREIGN KEY(vtname) REFERENCES VehicleType
);


CREATE TABLE Customer(
  dlicense  VARCHAR2(20) PRIMARY KEY,
  cellphone NUMBER,
  name      VARCHAR2(20),
  address   VARCHAR2(20)
);

CREATE TABLE Reservation(
	confNo			  NUMBER PRIMARY KEY,
  vtname			  VARCHAR2(20) NOT NULL,
  dlicense		  VARCHAR2(20) NOT NULL,
  fromTimestamp	TIMESTAMP,
  toTimestamp   TIMESTAMP,
  FOREIGN KEY (vtname) REFERENCES VehicleType,
  FOREIGN KEY (dlicense) REFERENCES Customer
);

CREATE TABLE Rental (
	rid 				  NUMBER PRIMARY KEY,
  vlicense 		  CHAR(6) NOT NULL,
  dlicense 	    VARCHAR2(20) NOT NULL,
  fromTimestamp	TIMESTAMP,
  toTimestamp	  TIMESTAMP,
  odometer		  NUMBER,
  cardName		  VARCHAR2(20),
  cardNo			  VARCHAR2(20),
  ExpDate			  DATE,
  confNo			  NUMBER,
  FOREIGN KEY (vlicense) REFERENCES Vehicle,
  FOREIGN KEY (dlicense) REFERENCES Customer,
  FOREIGN KEY (confNo) REFERENCES Reservation
);


CREATE TABLE Return(
	rid       NUMBER PRIMARY KEY,
  stamp     TIMESTAMP,
  odometer  NUMBER,
  fulltank  NUMBER,
  value NUMBER,
  FOREIGN KEY(rid) REFERENCES Rental
);