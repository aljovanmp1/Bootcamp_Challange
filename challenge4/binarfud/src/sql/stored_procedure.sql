create or replace procedure delete_user_by_id(id_in uuid)
language plpgsql    
as $$
begin
    delete from users where id = id_in;
    commit;
end;$$

call delete_user_by_id('4842c51f-c307-46e6-a1cc-892614f4a861')

create or replace procedure set_pwd_by_user_id(id_in uuid, pwd varchar)
language plpgsql    
as $$
begin
    update users 
    set "password" = pwd
    where id = id_in;
    commit;
end;$$

create or replace procedure set_email_by_user_id(id_in uuid, email varchar)
language plpgsql    
as $$
begin
    update users 
    set email_address = email
    where id = id_in;
    commit;
end;$$