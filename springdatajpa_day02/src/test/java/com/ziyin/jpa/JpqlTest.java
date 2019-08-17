package com.ziyin.jpa;

import com.ziyin.bo.Customer;
import com.ziyin.dao.CustomerDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author ziyin
 * @create 2019-08-09 9:09
 */
@RunWith(SpringJUnit4ClassRunner.class) //声明Spring提供的单元测试环境
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class JpqlTest {

	@Autowired
	private CustomerDao customerDao;


	@Test
	public void  testFindCustomerByName() {
		Customer customer = customerDao.findCustomerByName("ziyin");
		System.out.println(customer);
	}

	@Test
	public void  testFindCustomerNameAndId() {
		Customer customer = customerDao.findCustomerNameAndId("ziyin",2L);
		System.out.println(customer);
	}

	/**
	 * 测试jpql的更新操作
	 *  * springDataJpa中使用jpql完成 更新/删除操作
	 *         * 需要手动添加事务的支持
	 *         * 默认会执行结束之后，回滚事务
	 *   @Rollback : 设置是否自动回滚
	 *          false | true
	 */
	@Test
	@Transactional //添加事务的支持
	@Rollback(value = false)
	public void testUpdateCustomer() {
		customerDao.updateCustomer(3l,"ziyin3");
	}


	//测试sql查询
	@Test
	public void testFindSql() {
		List<Object[]> list = customerDao.findSql("ziyin%");
		for(Object[] obj : list) {
			System.out.println(Arrays.toString(obj));
		}
	}


	//测试方法命名规则的查询
	@Test
	public void testNaming() {
		Customer customer = customerDao.findByCustName("ziyin");
		System.out.println(customer);
	}



}
