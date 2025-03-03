CREATE TABLE Alumno (
    Matricula NUMBER NOT NULL PRIMARY KEY,
    Asignacion NUMBER NOT NULL ,
    Nombre VARCHAR2(50) NOT NULL ,
    Edad NUMBER  
);

INSERT INTO Alumno (Matricula, Asignacion, Nombre, Edad) VALUES (2230037, 1, 'Cesar', 123);
INSERT INTO Alumno (Matricula, Asignacion, Nombre, Edad) VALUES (2230040, 2, 'Ale', 123);
CREATE TABLE Salon (
    IdSalon NUMBER NOT NULL PRIMARY KEY,
    Matricula_FK_Alumno NUMBER NOT NULL ,
    FOREIGN KEY (Matricula_FK_Alumno) REFERENCES Alumno(Matricula)
);

INSERT INTO Salon (IdSalon, Matricula_FK_Alumno) VALUES (123, 2230037);
INSERT INTO Salon (IdSalon, Matricula_FK_Alumno) VALUES (456, 2230036);
