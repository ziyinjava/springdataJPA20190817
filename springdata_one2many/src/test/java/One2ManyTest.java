import com.ziyin.bo.Customer;
import com.ziyin.bo.LinkMan;
import com.ziyin.dao.CustomerDao;
import com.ziyin.dao.LinkManDao;
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
 * @create 2019-08-09 14:26
 */
@RunWith(SpringJUnit4ClassRunner.class) //声明Spring提供的单元测试环境
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class One2ManyTest {

	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private LinkManDao linkManDao;

	/**
	 * Hibernate: insert into cst_customer (cust_address, cust_industry, cust_level, cust_name, cust_phone, cust_source) values (?, ?, ?, ?, ?, ?)
	 * Hibernate: insert into cst_linkman (lkm_cust_id, lkm_email, lkm_gender, lkm_memo, lkm_mobile, lkm_name, lkm_phone, lkm_position) values (?, ?, ?, ?, ?, ?, ?, ?)
	 * Hibernate: update cst_linkman set lkm_cust_id=? where lkm_id=?
	 *  因为是更新维护外键的所以，插入的时候，外键的约束不能为null 需要去掉
	 */
	@Test
	@Transactional //配置事务
	@Rollback(false) //不自动回滚
	public void testAdd() {
		//创建一个客户，创建一个联系人
		Customer customer = new Customer();
		customer.setCustName("百度");

		LinkMan linkMan = new LinkMan();
		linkMan.setLkmName("小李");

		/**
		 * 配置了客户到联系人的关系
		 *      从客户的角度上：发送两条insert语句，发送一条更新语句更新数据库（更新外键）
		 * 由于我们配置了客户到联系人的关系：客户可以对外键进行维护
		 */
		customer.getLinkMans().add(linkMan);


		customerDao.save(customer);
		linkManDao.save(linkMan);
	}


	/**
	 * Hibernate: insert into cst_customer (cust_address, cust_industry, cust_level, cust_name, cust_phone, cust_source) values (?, ?, ?, ?, ?, ?)
	 * Hibernate: insert into cst_linkman (lkm_cust_id, lkm_email, lkm_gender, lkm_memo, lkm_mobile, lkm_name, lkm_phone, lkm_position) values (?, ?, ?, ?, ?, ?, ?, ?)
	 */
	@Test
	@Transactional //配置事务
	@Rollback(false) //不自动回滚
	public void testAdd1() {
		//创建一个客户，创建一个联系人
		Customer customer = new Customer();
		customer.setCustName("百度");

		LinkMan linkMan = new LinkMan();
		linkMan.setLkmName("小李");

		/**
		 * 配置联系人到客户的关系（多对一）
		 *    只发送了两条insert语句
		 * 由于配置了联系人到客户的映射关系（多对一）
		 *
		 *
		 */
		linkMan.setCustomer(customer);

		customerDao.save(customer);
		linkManDao.save(linkMan);
	}


	/**
	 * 会有一条多余的update语句
	 *      * 由于一的一方可以维护外键：会发送update语句
	 *      * 解决此问题：只需要在一的一方放弃维护权即可
	 *
	 */
	@Test
	@Transactional //配置事务
	@Rollback(false) //不自动回滚
	public void testAdd2() {
		//创建一个客户，创建一个联系人
		Customer customer = new Customer();
		customer.setCustName("百度");

		LinkMan linkMan = new LinkMan();
		linkMan.setLkmName("小李");

		linkMan.setCustomer(customer);//由于配置了多的一方到一的一方的关联关系（当保存的时候，就已经对外键赋值）
		customer.getLinkMans().add(linkMan);//由于配置了一的一方到多的一方的关联关系（发送一条update语句）

		customerDao.save(customer);
		linkManDao.save(linkMan);
	}

	/**
	 * 级联添加：保存一个客户的同时，保存客户的所有联系人
	 *      需要在操作主体的实体类上，配置casacde属性
	 */
	@Test
	@Transactional //配置事务
	@Rollback(false) //不自动回滚
	public void testCascadeAdd() {
		Customer customer = new Customer();
		customer.setCustName("百度1");

		LinkMan linkMan = new LinkMan();
		linkMan.setLkmName("小李1");

		linkMan.setCustomer(customer);
		customer.getLinkMans().add(linkMan);

		customerDao.save(customer);
	}

	/**
	 * 级联删除：
	 *      删除1号客户的同时，删除1号客户的所有联系人
	 *
		Hibernate: select customer0_.cust_id as cust_id1_0_0_, customer0_.cust_address as cust_add2_0_0_, customer0_.cust_industry as cust_ind3_0_0_, customer0_.cust_level as cust_lev4_0_0_, customer0_.cust_name as cust_nam5_0_0_, customer0_.cust_phone as cust_pho6_0_0_, customer0_.cust_source as cust_sou7_0_0_ from cst_customer customer0_ where customer0_.cust_id=?
	 * Hibernate: select linkmans0_.lkm_cust_id as lkm_cust9_1_0_, linkmans0_.lkm_id as lkm_id1_1_0_, linkmans0_.lkm_id as lkm_id1_1_1_, linkmans0_.lkm_cust_id as lkm_cust9_1_1_, linkmans0_.lkm_email as lkm_emai2_1_1_, linkmans0_.lkm_gender as lkm_gend3_1_1_, linkmans0_.lkm_memo as lkm_memo4_1_1_, linkmans0_.lkm_mobile as lkm_mobi5_1_1_, linkmans0_.lkm_name as lkm_name6_1_1_, linkmans0_.lkm_phone as lkm_phon7_1_1_, linkmans0_.lkm_position as lkm_posi8_1_1_ from cst_linkman linkmans0_ where linkmans0_.lkm_cust_id=?
	 * Hibernate: delete from cst_linkman where lkm_id=?
	 * Hibernate: delete from cst_customer where cust_id=?
	 */
	@Test
	@Transactional //配置事务
	@Rollback(false) //不自动回滚
	public void testCascadeRemove() {
		//1.查询1号客户
		Optional<Customer> customer = customerDao.findById(12l);
		//2.删除1号客户
		customerDao.delete(customer.get());
	}
}
