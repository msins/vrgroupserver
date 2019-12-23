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

create table Choice
(
    id         int auto_increment
        primary key,
    questionId int                      not null,
    orderValue int                      not null,
    text       varchar(50) charset utf8 not null
);

create table Scenario
(
    id   int auto_increment
        primary key,
    name varchar(50) not null
);

create table ScenarioQuestion
(
    scenarioId int not null,
    questionId int not null,
    primary key (scenarioId, questionId)
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
    IP               varchar(39) null,
    primary key (userId, gameId, scenarioId, questionId, timestampCreated)
);
