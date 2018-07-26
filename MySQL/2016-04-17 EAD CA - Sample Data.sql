-- 30-03-2016 CA Script File Enterprise App Dev

-- Create Database
CREATE DATABASE IF NOT EXISTS JoeCA;
USE JoeCA;


-- Drop Tables
DROP TABLE IF EXISTS Administrators;
DROP TABLE IF EXISTS Attendees;
DROP TABLE IF EXISTS CustSched; -- drop before workshops
DROP TABLE IF EXISTS Schedule; -- drop before workshops
DROP TABLE IF EXISTS Workshops;
DROP TABLE IF EXISTS Exhibitors;
DROP TABLE IF EXISTS Speakers;


-- Create Tables
CREATE TABLE IF NOT EXISTS Administrators(
admin_username CHAR(40) PRIMARY KEY, 
admin_password CHAR(40) NOT NULL, 
admin_fname CHAR(40), 
admin_lname CHAR(40), 
admin_email VARCHAR(60) NOT NULL, 
admin_phone VARCHAR(18), 
admin_addr1 CHAR(40), 
admin_addr2 CHAR(40), 
admin_town CHAR(40), 
admin_county CHAR(40));

CREATE TABLE IF NOT EXISTS Attendees(
attendee_fname CHAR(40) NOT NULL, 
attendee_lname CHAR(40) NOT NULL, 
attendee_email CHAR(40) PRIMARY KEY, 
attendee_phone CHAR(40), 
attendee_addr1 CHAR(40), 
attendee_addr2 CHAR(40), 
attendee_town CHAR(40), 
attendee_county CHAR(40));

CREATE TABLE IF NOT EXISTS Workshops(
ws_id INT PRIMARY KEY AUTO_INCREMENT,
ws_name VARCHAR(60) NOT NULL,
ws_presenter1 CHAR(40) NOT NULL,
ws_presenter2 CHAR(40),
ws_info TEXT NOT NULL);

CREATE TABLE IF NOT EXISTS Schedule(
schedule_time TIME PRIMARY KEY, 
workshop_id INT NOT NULL, 
schedule_location CHAR(40),
CONSTRAINT fk_shedule_workshop FOREIGN KEY (workshop_id) REFERENCES workshops (ws_id));

CREATE TABLE IF NOT EXISTS CustSched(
workshop_id INT PRIMARY KEY, 
CONSTRAINT fk_custsched_workshop FOREIGN KEY (workshop_id) REFERENCES schedule (workshop_id));

CREATE TABLE IF NOT EXISTS Exhibitors(
exhibitor_id INT PRIMARY KEY AUTO_INCREMENT, 
exhibitor_fname CHAR(40) NOT NULL, 
exhibitor_lname CHAR(40) NOT NULL, 
exhibitor_bio TEXT NOT NULL, 
exhibitor_website VARCHAR(60), 
exhibitor_pic VARCHAR(60));        

CREATE TABLE IF NOT EXISTS Speakers(
speaker_id INT PRIMARY KEY AUTO_INCREMENT, 
speaker_fname CHAR(40) NOT NULL, 
speaker_lname CHAR(40) NOT NULL, 
speaker_bio TEXT NOT NULL, 
speaker_website VARCHAR(60),
speaker_pic VARCHAR(60));


-- Populate Tables
-- Administrators
INSERT INTO Administrators VALUES ('Joe','password','Joe','O''Regan','joe.oregan@e.mail','085-1234567','Monakeeba','','Thurles','Tipperary');
INSERT INTO Administrators VALUES ('Pam','password','Pamela','O''Brien','pam.obrien@e.mail','086-2345678','LIT','Racecourse Rd','Thurles','Tipperary');

-- Speakers
INSERT INTO Speakers(speaker_fname,speaker_lname,speaker_bio,speaker_website,speaker_pic)
VALUES('Joe','O''Regan','<b>Student No: </b>K00203642 <br><br>Joe is the creater of this website. <br>Joe has too much time on his hands','https://www.facebook.com/joeoregan2','/images/troll.jpg'); 
INSERT INTO Speakers(speaker_fname,speaker_lname,speaker_bio,speaker_website,speaker_pic)
VALUES('Sean','Horgan','Sean sits next to me in Enterprise App Development. <br>And he doesn''t yap yap yap all day like Paul. <br><br>Sometimes I wish I had no ears!!!','https://www.facebook.com/schorgan2','/images/dexter.jpg'); 
INSERT INTO Speakers(speaker_fname,speaker_lname,speaker_bio,speaker_website,speaker_pic)
VALUES('Daniel','Gadd','Daniel is a keynote speaker. <br>Why? I don''t know...<br><br>He can just waffle on about anything.<br><br><b><< This is not Daniel Gadd!</b><br><div style="padding-left: 30px;">(He''s not that cool)</div>.','https://www.facebook.com/atomicdanielgadd','/images/sloth.jpg'); 
INSERT INTO Speakers(speaker_fname,speaker_lname,speaker_bio,speaker_website,speaker_pic)
VALUES('Eoin','O''Sullivan','Sully loves midgets, this has nothing got to do with ICT.<br>It''s just a random fact! <br>Here is a random picture of a potato...<br><img src="http://s12.postimg.org/yxzdplmjx/potato.jpg" style="padding-left: 60px; alt="Random Potato" height="90" width="90">','https://www.facebook.com/SUSU.sullyosullivan','/images/nerd3.jpg');
INSERT INTO Speakers(speaker_fname,speaker_lname,speaker_bio,speaker_website,speaker_pic)
VALUES('Stevie','Dineen','Steve is from Kerry.<br>That is all.','www.stevied.com','/images/nerd.jpg');
INSERT INTO Speakers(speaker_fname,speaker_lname,speaker_bio,speaker_website,speaker_pic)
VALUES('Paul','O''Brien','Paul is a creepy Amish looking guy that sits next to me in EAD.<br>Paul goes on and on and on <br>and on and on and on <br>and on <br>and on<br>and on<br>and on <br>and on<br>and on<br>and on <br>and on<br>and on<br>and on <br>and on<br>and on<br>and on <br>and on<br>and on<br>and on <br>and on<br>and on<br>and on <br>and on<br>and on<br>and on <br>and on<br>and on<br>...about any old nonsense','www.justshutup.com','/images/nerdb.jpg'); 
INSERT INTO Speakers(speaker_fname,speaker_lname,speaker_bio,speaker_website,speaker_pic)
VALUES('Brain','Ryan','Brain does a lot of deep thinking','www.ibraingood.com','/images/example.jpg'); 

-- Workshops
INSERT INTO Workshops(ws_name,ws_presenter1,ws_info) VALUES('Break','none','Break Times:\n8am Begin\n10 a.m. - 10.30 a.m. Break\n1 p.m. - 2 p.m. Lunch\n4 p.m. - 4.30 p.m. Break');
INSERT INTO Workshops(ws_name,ws_presenter1,ws_presenter2,ws_info) VALUES('Events Begin. Introduction to the days events','Joe O''Regan','','A run-down of the days events and introduction');
INSERT INTO Workshops(ws_name,ws_presenter1,ws_presenter2,ws_info) VALUES('The internet of things', 'Joe O''Regan', 'Daniel Gadd', 'Connecting gizmos and gadgets to the interweb');
INSERT INTO Workshops(ws_name,ws_presenter1,ws_presenter2,ws_info) VALUES('Java for dummies', 'Sean Horgan', 'Stevie Dineen', 'Java stuff for dumbasses, by dumbasses');
INSERT INTO Workshops(ws_name,ws_presenter1,ws_presenter2,ws_info) VALUES('ICT Skills','Brian Ryan','','Random ICT skills!!!');
INSERT INTO Workshops(ws_name,ws_presenter1,ws_presenter2,ws_info) VALUES('Global Technology Hub', 'Stevie Dineen', 'Brian Ryan', 'Interconnected Random ICT stuff ...maybe');
INSERT INTO Workshops(ws_name,ws_presenter1,ws_presenter2,ws_info) VALUES('Literacy and Technology', 'Christina Costello', 'Anthony Esmonde', 'Random ICT stuff that don''t read good');
INSERT INTO Workshops(ws_name,ws_presenter1,ws_presenter2,ws_info) VALUES('Technology in Schools', 'Tom Ferris', 'G Liutkus', 'Random ICT stuff in the classroom');
INSERT INTO Workshops(ws_name,ws_presenter1,ws_presenter2,ws_info) VALUES('Coding for gamers', 'Paul O''Brien', 'Sgt. Ed Craker', 'Random ICT stuff with levels and a high-score table');
INSERT INTO Workshops(ws_name,ws_presenter1,ws_presenter2,ws_info) VALUES('Web Applications and Development', 'Joe O''Regan', 'Dominik Pastusiak', 'Web Applications and Development type Random ICT stuff');
INSERT INTO Workshops(ws_name,ws_presenter1,ws_presenter2,ws_info) VALUES('Digital Strategy for Small Business','Joe Dowd','','Small Business Random ICT Stuff');
INSERT INTO Workshops(ws_name,ws_presenter1,ws_presenter2,ws_info) VALUES('Research', 'Daniel Gadd', 'Emily Barnett', 'Random ICT stuff');
INSERT INTO Workshops(ws_name,ws_presenter1,ws_presenter2,ws_info) VALUES('Closing Q & A', 'Dominik Pastusiak', 'Brian Ryan', 'Questions, lots of questions and very little answers about Random ICT stuff');

-- Exhibitors
INSERT INTO exhibitors (exhibitor_fname, exhibitor_lname, exhibitor_bio, exhibitor_website, exhibitor_pic)
VALUES ('Joe', 'O''Regan', 'Creater of this website', 'www.blah.com', '/images/troll.jpg');
INSERT INTO exhibitors (exhibitor_fname, exhibitor_lname, exhibitor_bio, exhibitor_website, exhibitor_pic)
VALUES ('Brian', 'Ryan', 'Brian does stuff', 'www.moreblah.com', '/images/nerd.jpg');
INSERT INTO exhibitors (exhibitor_fname, exhibitor_lname, exhibitor_bio, exhibitor_website, exhibitor_pic)
VALUES ('Daniel', 'Gadd', 'Daniel is an expert in guessing repeatedly, until he gets the right answer', 'www.gaddmaster.com', '/images/sloth.jpg');

-- Schedule
-- First: Initialise table with breaks
INSERT INTO schedule(schedule_time,workshop_id,schedule_location) values('100000', 1, 'Break');
INSERT INTO schedule(schedule_time,workshop_id,schedule_location) values('130000', 1, 'Lunch');
INSERT INTO schedule(schedule_time,workshop_id,schedule_location) values('160000', 1, 'Break');
-- Then
insert into schedule(schedule_time,workshop_id,schedule_location) values('080000', 2, 'Room A101');
insert into schedule(schedule_time,workshop_id,schedule_location) values('083000', 3, 'Room B102');
insert into schedule(schedule_time,workshop_id,schedule_location) values('090000', 4, 'Room A103');
insert into schedule(schedule_time,workshop_id,schedule_location) values('093000', 5, 'Room C102');
insert into schedule(schedule_time,workshop_id,schedule_location) values('103000', 6, 'Room A101');
insert into schedule(schedule_time,workshop_id,schedule_location) values('113000', 7, 'Room C103'); 
insert into schedule(schedule_time,workshop_id,schedule_location) values('120000', 8, 'Room B101');
insert into schedule(schedule_time,workshop_id,schedule_location) values('123000', 9, 'Room B102');
insert into schedule(schedule_time,workshop_id,schedule_location) values('140000', 10, 'Room C103');
insert into schedule(schedule_time,workshop_id,schedule_location) values('150000', 11, 'Room A103');
insert into schedule(schedule_time,workshop_id,schedule_location) values('163000', 12, 'Room B101');
insert into schedule(schedule_time,workshop_id,schedule_location) values('170000', 13, 'Room B102'); 

-- Test Data
-- UPDATE Exhibitors SET exhibitor_fname = 'Daniel' WHERE exhibitor_id = 3;
-- SELECT ws_id FROM Workshops WHERE ws_id !=1; -- Clear Schedule
-- DELETE FROM CustSched WHERE workshop_id !=1;
-- DELETE FROM Schedule WHERE schedule_time NOT IN ('100000','130000','160000');
