package ru.workspace.mbraw.webapp.dao.impl;

import org.springframework.stereotype.Repository;
import ru.workspace.mbraw.webapp.dao.IPlatformDAO;
import ru.workspace.mbraw.webapp.pojo.Platform;

@Repository
public class PlatformDAO extends GenericDAO<Platform, Integer> implements IPlatformDAO {

}
