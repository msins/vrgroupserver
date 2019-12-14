create table Game
(
    id   int auto_increment
        primary key,
    name varchar(200) not null
);

create table Question
(
    id   int auto_increment
        primary key,
    text varchar(300) not null
);

create table GameQuestion
(
    gameId     int not null,
    questionId int not null,
    primary key (gameId, questionId)
--     constraint GameQuestion_Game_id_fk
--         foreign key (gameId) references Game (id)
--             on update cascade on delete cascade,
--     constraint GameQuestion_Question_id_fk
--         foreign key (questionId) references Question (id)
--             on update cascade on delete cascade
);

create table Choice
(
    id         int auto_increment
        primary key,
    questionId int                      not null,
    orderValue int                      not null,
    text       varchar(50) charset utf8 not null
--     constraint Choice_Question_Id_fk
--         foreign key (questionId) references Question (Id)
--             on update cascade on delete cascade
);

create table Scenario
(
    id   int auto_increment
        primary key,
    name varchar(50) not null
);

create table GameScenario
(
    gameId     int not null,
    scenarioId int not null,
    primary key (gameId, scenarioId)
);

create table User
(
    id     int auto_increment
        primary key,
    name   varchar(50) not null,
    email  varchar(50) null,
    gender char        null,
    age    int         null
);

create table Answer
(
    userId           int         not null,
    gameId           int         not null,
    scenarioId       int         not null,
    questionId       int         not null,
    choiceId         int         not null,
    timestampCreated timestamp   not null,
    IPv4             varchar(16) null,
    primary key (userId, gameId, scenarioId, questionId, timestampCreated)
--     constraint Answer_Game_id_fk
--         foreign key (gameId) references Game (id)
--             on update cascade,
--     constraint Answer_Question_id_fk
--         foreign key (questionId) references Question (id)
--             on update cascade,
--     constraint Answer_Scenario_id_fk
--         foreign key (scenarioId) references Scenario (id)
--             on update cascade,
--     constraint Answer_User_id_fk
--         foreign key (userId) references User (id)
--             on update cascade,
--     constraint Answer_Choice_id_fk
--         foreign key (choiceId) references Choice (id)
--             on update cascade
);

insert into Scenario values(1, 'Default');
