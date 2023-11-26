ALTER TABLE ratings
    drop id;
ALTER TABLE ratings  ADD id INTEGER NOT NULL primary key auto_increment