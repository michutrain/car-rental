INSERT ALL
    Into VehicleType(vtname, features, wrate, drate, hrate, wirate, dirate, hirate, krate) VALUES
        ('Midsize', 'Heated seats, electric windows', 220, 60, 12, 10, 5, 2.5, 0.4)
    Into VehicleType(vtname, features, wrate, drate, hrate, wirate, dirate, hirate, krate) VALUES
        ('SUV', 'AWD, Heated seats, electric windows', 310, 80, 20, 15, 3, 1, 2)
    Into VehicleType(vtname, features, wrate, drate, hrate, wirate, dirate, hirate, krate) VALUES
        ('Economy', '2Door, 2WD', 190, 50, 10, 9, 4, 2, 0.4)
    Into VehicleType(vtname, features, wrate, drate, hrate, wirate, dirate, hirate, krate) VALUES
        ('Compact', '2Door, 2WD', 150, 40, 8, 5, 3, 1, 0.2)
    Into VehicleType(vtname, features, wrate, drate, hrate, wirate, dirate, hirate, krate) VALUES
        ('Standard', '2Door, 2WD', 230, 65, 12, 6, 3, 1, 1)
    Into VehicleType(vtname, features, wrate, drate, hrate, wirate, dirate, hirate, krate) VALUES
        ('Fullsize', '2Door, 2WD', 270, 75, 15, 7.5, 3, 1.2, 1.5)
    Into VehicleType(vtname, features, wrate, drate, hrate, wirate, dirate, hirate, krate) VALUES
        ('Truck', '200 HP, 4WD', 310, 90, 22, 8, 4, 2, 2.5)

SELECT * FROM dual;

INSERT ALL
    Into Customer(dlicense, cellphone, name, address) VALUES
        ('3156132', '6041234567', 'Bob Vance', '123 SomeStreet, Vancouver')
    Into Customer(dlicense, cellphone, name, address) VALUES
        ('001', '16049181234', 'Drew Carey', '456, OtherStreet, Vancouver BC')
    Into Customer(dlicense, cellphone, name, address) VALUES
        ('002', '275341920912', 'John Doe', '3718 Marine Dr. Vancouver BC Canada')
    Into Customer(dlicense, cellphone, name, address) VALUES
        ('003', '911', 'Jane Doe', '123 NoneOfYourBusiness St.')
    Into Customer(dlicense, cellphone, name, address) VALUES
        ('004', '16047778888', 'Amy', '123 Yonge St. Toronto ON')
    Into Customer(dlicense, cellphone, name, address) VALUES
        ('005', '1827392821', 'Serial Joe', 'Address of 005')
    Into Customer(dlicense, cellphone, name, address) VALUES
        ('006', '7819281928', 'Will Ferrell', 'Address of 006')
    Into Customer(dlicense, cellphone, name, address) VALUES
        ('007', '1', 'Feral animal', 'Address of 007')
    Into Customer(dlicense, cellphone, name, address) VALUES
        ('008', '1234567890', 'George Bush', 'White House')
    Into Customer(dlicense, cellphone, name, address) VALUES
        ('009', '1234567880', 'George W Bush', 'White House')
    SELECT * FROM dual;

INSERT ALL
    Into Vehicle(vid, vlicense, make, model, year, color, odometer, status, vtname, branch) VALUES
        (1, '123456', 'Toyota', 'Camry', 1998, 'Yellow', 20981, 0, 'Economy', 'Robson St. - Vancouver')
    Into Vehicle(vid, vlicense, make, model, year, color, odometer, status, vtname, branch) VALUES
        (2, '789101', 'Toyota', 'Camry', 1999, 'Yellow', 20981, 0, 'Economy', 'YVR - Vancouver')
    Into Vehicle(vid, vlicense, make, model, year, color, odometer, status, vtname, branch) VALUES
        (3, '1121314', 'Honda', 'Civic', 2000, 'Yellow', 20981, 0, 'Standard', 'Granville St. - Vancouver')
    Into Vehicle(vid, vlicense, make, model, year, color, odometer, status, vtname, branch) VALUES
        (4, '1516171', 'Lexus', 'A', 2001, 'Yellow', 20981, 0, 'Midsize', 'Robson St. - Vancouver')
    Into Vehicle(vid, vlicense, make, model, year, color, odometer, status, vtname, branch) VALUES
        (5, '8192021', 'Tesla', 'Model S', 2002, 'Yellow', 20981, 1, 'Standard', 'Granville St. - Vancouver')
    Into Vehicle(vid, vlicense, make, model, year, color, odometer, status, vtname, branch) VALUES
        (6, '222324', 'Tesla', 'Model 3', 2003, 'Yellow', 20981, 0, 'Fullsize', 'Robson St. - Vancouver')
    Into Vehicle(vid, vlicense, make, model, year, color, odometer, status, vtname, branch) VALUES
        (7, '2526262', 'Ford', 'F150', 2004, 'Yellow', 20981, 1, 'Truck', 'Robson St. - Vancouver')
    Into Vehicle(vid, vlicense, make, model, year, color, odometer, status, vtname, branch) VALUES
        (8, '1A1 B1S', 'Smart Car', 'Smart Car', 2005, 'Black', 20981, 0, 'Compact', 'YVR - Vancouver')
    Into Vehicle(vid, vlicense, make, model, year, color, odometer, status, vtname, branch) VALUES
        (9, 'ABCDEF', 'Lincoln', 'Memorial', 2006, 'Blue', 20981, 0, 'Fullsize', 'Robson St. - Vancouver')
    Into Vehicle(vid, vlicense, make, model, year, color, odometer, status, vtname, branch) VALUES
        (192839, 'SomeValue', 'FutureCar', '9000', 2106, 'VantaBlack', 1000000, 3, 'Fullsize', 'Yonge St. - Toronto')

  SELECT * FROM dual;


INSERT ALL
    Into Reservation(confNo, vtname, dlicense, fromTimestamp, toTimestamp) VALUES
        (1, 'Economy', '002', TIMESTAMP '1998-12-17 12:00:00', TIMESTAMP '1998-12-18 13:00:00')
    Into Reservation(confNo, vtname, dlicense, fromTimestamp, toTimestamp) VALUES
        (2, 'Fullsize', '005', TIMESTAMP '2019-11-17 12:00:00', TIMESTAMP '2019-11-20 12:00:00')
    Into Reservation(confNo, vtname, dlicense, fromTimestamp, toTimestamp) VALUES
        (3, 'Truck', '004', TIMESTAMP '2019-12-18 15:00:00', TIMESTAMP '2019-12-20 23:59:59')
    Into Reservation(confNo, vtname, dlicense, fromTimestamp, toTimestamp) VALUES
        (4, 'Economy', '007', TIMESTAMP '2019-12-04 6:00:00', TIMESTAMP '2020-01-02 12:00:00')
    Into Reservation(confNo, vtname, dlicense, fromTimestamp, toTimestamp) VALUES
        (5, 'Compact', '3156132', TIMESTAMP '2019-12-05 12:00:00', TIMESTAMP '2020-12-18 12:00:00')
    Into Reservation(confNo, vtname, dlicense, fromTimestamp, toTimestamp) VALUES
        (6, 'Standard', '003', TIMESTAMP '2019-12-17 12:00:00', TIMESTAMP '2019-12-20 12:00:00')
    Into Reservation(confNo, vtname, dlicense, fromTimestamp, toTimestamp) VALUES
        (7, 'Midsize', '002', TIMESTAMP '2020-01-01 12:30:00', TIMESTAMP '2020-01-21 12:00:00')

SELECT * FROM dual;

INSERT ALL
    Into Rental(rid, vid, dlicense, fromTimestamp, toTimestamp, odometer) VALUES
         (8, 1, '001', TIMESTAMP '1998-12-17 12:00:00', TIMESTAMP'1998-12-18 13:00:00', 100)
    Into Rental(rid, vid, dlicense, fromTimestamp, toTimestamp, odometer) VALUES
         (9, 5, '009', TIMESTAMP '2019-12-17 12:00:00', TIMESTAMP '2019-12-18 13:00:00', 100)
    Into Rental(rid, vid, dlicense, fromTimestamp, toTimestamp, odometer) VALUES
         (10, 192839, '007', TIMESTAMP '2019-12-17 12:00:00', TIMESTAMP '2019-12-29 13:00:00', 100)

SELECT * FROM dual;

INSERT INTO Return(rid, vid, stamp, value) VALUES (9, 5, SYSTIMESTAMP, 200);
Insert Into Return(rid, vid, stamp, value) VALUES (8, 1, SYSTIMESTAMP, 28);

