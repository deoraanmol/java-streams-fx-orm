-- No Of Tables - 9 --

-- STUDENT INFO--
create table technical_skills
(
    id int not null auto_increment primary key,
    p  int not null,
    n  int not null,
    a  int not null,
    w  int not null
);

create table students
(
    id varchar(5) primary key,
    skillset int not null,
    foreign key (skillset) references technical_skills (id)
);

create table student_info
(
    id               int primary key auto_increment,
    student_id       varchar(5) unique not null,
    skillset         int not null ,
    personality_type varchar(1) not null,
    conflict1        varchar(5) not null ,
    conflict2        varchar(5) not null ,
    foreign key (skillset) references technical_skills(id),
    foreign key (student_id) references students (id)
);
-- STUDENT INFO ENDS--
-- COMPANIES --
create table companies
(
    id  int not null primary key auto_increment,
    name  varchar(250) not null,
    abn_number  varchar(250) not null,
    url  varchar(250),
    address  varchar(250)
);

create table owners
(
    id varchar(6) not null primary key,
    first_name  varchar(20) not null,
    surname  varchar(20) not null,
    role  varchar(50) not null,
    email  varchar(50) not null,
    company_id  int not null,
    foreign key (company_id) references companies (id)
);

-- COMPANIES END--
-- PROJECT --
create table project_skills
(
    id int not null auto_increment primary key,
    p  int not null,
    n  int not null,
    a  int not null,
    w  int not null
);

create table projects
(
    id               varchar(5) primary key,
    title            varchar(50) not null,
    description      varchar(100) not null,
    owner            varchar(6) not null,
    skillset         int not null,
    foreign key (skillset) references project_skills (id),
    foreign key (owner) references owners (id)
);
-- PROJECT ENDS --

-- PROJECT PREFERENCE --
create table project_preference
(
    id               int not null auto_increment primary key,
    student_id       varchar(5) unique not null,
    preferences_string varchar(100) not null,
    preference1       varchar(5) not null,
    preference2       varchar(5) not null,
    foreign key (student_id) references students(id),
    foreign key pref1 (preference1) references projects(id),
    foreign key pref2 (preference2) references projects(id)
);
-- PROJECT PREFERENCE ENDS --

-- TEAMS --
create table teams
(
    id int not null auto_increment primary key,
    project_id varchar(5) unique not null,
    student_id_1 varchar(5),
    student_id_2 varchar(5),
    student_id_3 varchar(5),
    student_id_4 varchar(5),
    foreign key (project_id) references projects(id),
    foreign key (student_id_1) references students(id),
    foreign key (student_id_2) references students(id),
    foreign key (student_id_3) references students(id),
    foreign key (student_id_4) references students(id)
);
-- TEAMS END --
