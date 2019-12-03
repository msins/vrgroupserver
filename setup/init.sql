create table Game
(
    id   int          not null
        primary key,
    name varchar(200) not null
);

create table Question
(
    id   int          not null
        primary key,
    text varchar(300) not null,
    type int          not null
);

create table GameQuestion
(
    gameId     int not null,
    questionId int not null,
    primary key (gameId, questionId),
    constraint FKhmguwsd98wkd1hkvbh3p8w98n
        foreign key (gameId) references Game (id),
    constraint GameQuestion_Game_id_fk
        foreign key (gameId) references Game (id)
            on update cascade on delete cascade,
    constraint GameQuestion_Question_id_fk
        foreign key (questionId) references Question (id)
            on update cascade on delete cascade,
    constraint FKgivi5j22s9ddh0qp8hu8v95ie
        foreign key (questionId) references Question (id)
);

create table MultipleChoicesQuestion
(
    questionId int not null
        primary key,
    constraint CustomAnswerQuestion_Question_id_fk
        foreign key (questionId) references Question (id)
            on update cascade on delete cascade,
    constraint FKi0m7iw02mw9nc59nl2pwjao4h
        foreign key (questionId) references Question (id)
);

create table Choice
(
    questionId int                      not null,
    text       varchar(50) charset utf8 not null,
    constraint Choice_MultipleChoicesQuestion_questionId_fk
        foreign key (questionId) references MultipleChoicesQuestion (questionId)
            on update cascade on delete cascade,
    constraint FK7eypwd6u5ibcprw5w6degts5v
        foreign key (questionId) references MultipleChoicesQuestion (questionId)
);

create table ScalingQuestion
(
    questionId int not null
        primary key,
    id         int not null,
    constraint FKfjlslye6smmiuhdacs5norg26
        foreign key (questionId) references Question (id),
    constraint ScalingQuestion_Question_id_fk
        foreign key (questionId) references Question (id)
            on update cascade on delete cascade
);

create table Scenario
(
    id   int         not null
        primary key,
    name varchar(50) not null
);

create table User
(
    id    int         not null
        primary key,
    name  varchar(50) not null,
    email varchar(50) null
);

create table Answer
(
    userId           int         not null,
    gameId           int         not null,
    scenarioId       int         not null,
    questionId       int         not null,
    timestampCreated timestamp   not null,
    score            int         not null,
    IPv4             varchar(16) null,
    primary key (userId, gameId, scenarioId, questionId, timestampCreated),
    constraint Answer_Game_id_fk
        foreign key (gameId) references Game (id)
            on update cascade,
    constraint Answer_Question_id_fk
        foreign key (questionId) references Question (id)
            on update cascade,
    constraint Answer_Scenario_id_fk
        foreign key (scenarioId) references Scenario (id)
            on update cascade,
    constraint Answer_User_id_fk
        foreign key (userId) references User (id)
            on update cascade,
    constraint FK1mjel3ucrxn459n1lup5uou85
        foreign key (questionId) references Question (id),
    constraint FK5qpn6qxm4lpv4hmvyqtid6f3t
        foreign key (scenarioId) references Scenario (id),
    constraint FKgfm0rs0wvxkuc9ebewkjdr6c4
        foreign key (gameId) references Game (id),
    constraint FKkhau9q52accu2q5h0b8t0ayt
        foreign key (userId) references User (id)
);

