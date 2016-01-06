package global;

import java.util.List;
public abstract class DAO {
	public int insert(Object o){return 0;}
	public int update(Object o){return 0;}
	public List selectAll(){return null;}
	public List selectByName(String name){return null;}
	public Object selectOneBy(String key){return null;}
	public abstract int delete(String id);
	public abstract int count();
}
