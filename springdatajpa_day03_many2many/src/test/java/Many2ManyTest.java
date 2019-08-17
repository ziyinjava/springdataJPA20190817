import com.ziyin.bo.Role;
import com.ziyin.bo.User;
import com.ziyin.dao.RoleDao;
import com.ziyin.dao.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author ziyin
 * @create 2019-08-09 17:17
 */
@RunWith(SpringJUnit4ClassRunner.class) //声明Spring提供的单元测试环境
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class Many2ManyTest {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;

	/**
	 * 保存一个用户，保存一个角色
	 *
	 *  多对多放弃维护权：被动的一方放弃
	 *
	 *  Hibernate: insert into sys_user (age, user_name) values (?, ?)
	 * Hibernate: insert into sys_role (role_name) values (?)
	 * Hibernate: insert into sys_user_role (sys_user_id, sys_role_id) values (?, ?)
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void  testAdd() {
		User user = new User();
		user.setUserName("小李");

		Role role = new Role();
		role.setRoleName("java程序员");

		//配置用户到角色关系，可以对中间表中的数据进行维护     1-1
		user.getRoles().add(role);

		//配置角色到用户的关系，可以对中间表的数据进行维护     1-1
		role.getUsers().add(user);

		userDao.save(user);
		roleDao.save(role);
	}

	//测试级联添加（保存一个用户的同时保存用户的关联角色）
	@Test
	@Transactional
	@Rollback(false)
	public void  testCasCadeAdd() {
		User user = new User();
		user.setUserName("小李");

		Role role = new Role();
		role.setRoleName("java程序员");

		//配置用户到角色关系，可以对中间表中的数据进行维护     1-1
		user.getRoles().add(role);

		//配置角色到用户的关系，可以对中间表的数据进行维护     1-1
		role.getUsers().add(user);

		userDao.save(user);
	}


	/**
	 * 案例：删除id为1的用户，同时删除他的关联对象
	 *
	 * Hibernate: select user0_.user_id as user_id1_1_0_, user0_.age as age2_1_0_, user0_.user_name as user_nam3_1_0_ from sys_user user0_ where user0_.user_id=?
	 * Hibernate: select roles0_.sys_user_id as sys_user1_2_0_, roles0_.sys_role_id as sys_role2_2_0_, role1_.role_id as role_id1_0_1_, role1_.role_name as role_nam2_0_1_ from sys_user_role roles0_ inner join sys_role role1_ on roles0_.sys_role_id=role1_.role_id where roles0_.sys_user_id=?
	 * Hibernate: delete from sys_user_role where sys_user_id=?
	 * Hibernate: delete from sys_role where role_id=?
	 * Hibernate: delete from sys_user where user_id=?
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void  testCasCadeRemove() {
		//查询1号用户
		Optional<User> user = userDao.findById(1l);
		//删除1号用户
		userDao.delete(user.get());

	}

}
