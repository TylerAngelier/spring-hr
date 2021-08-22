declare
    l_count number;
    l_tablespace varchar2(100 char) := 'USERS';
begin

    select count('x')
    into l_count
    from all_users
    where username = 'HR';

    if (l_count = 0) then
        execute immediate '
            create user hr identified by "super-secret-password"
        ';
    end if;

end;
/