package com.ziyin.jpa;

import com.ziyin.bo.Customer;
import com.ziyin.dao.CustomerDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author ziyin
 * @create 2019-08-08 16:35
 */
@RunWith(SpringJUnit4ClassRunner.class) //声明Spring提供的单元测试环境
@ContextConfiguration(locations = "classpath:applicationContext.xml") //指定Spring容器的配置信息
public class CustomerDaoTest {

	@Autowired
	private CustomerDao customerDao;

	/**
	 * 根据id查询
	 */
	@Test
	public void testFindOne(){
		Optional<Customer> customer = customerDao.findById(1L);
		System.out.println(customer.get());
	}

	/**
	 * save : 保存或者更新
	 *      根据传递的对象是否存在主键id，
	 *      如果没有id主键属性：保存
	 *      存在id主键属性，根据id查询数据，更新数据
	 */
	@Test
	public void testSave(){
		Customer customer  = new Customer();
		customer.setCustName("ziyin");
		customer.setCustLevel("vip");
		customer.setCustIndustry("it公司");
		customerDao.save(customer);
	}

	@Test
	public void testUpdate() {
		Customer customer  = new Customer();
		customer.setCustId(1L);
		customer.setCustName("ziyin666");
		customerDao.save(customer);
	}

	@Test
	public void testDelete () {
		customerDao.deleteById(1L);
	}


	/**
	 * 查询所有
	 */
	@Test
	public void testFindAll () {
		List<Customer> list = customerDao.findAll();
		for (Customer customer : list) {
			System.out.println(customer);
		}
	}

	/**
	 * 测试统计查询：查询客户的总数量
	 *      count:统计总条数
	 */
	@Test
	public void testCount() {
		long count = customerDao.count();//查询全部的客户数量
		System.out.println(count);
	}


	/**
	 * 测试：判断id为4的客户是否存在
	 *      1. 可以查询下id为4的客户
	 *          如果值为空，代表不存在，如果不为空，代表存在
	 *      2. 判断数据库中id为4的客户的数量
	 *          如果数量为0，代表不存在，如果大于0，代表存在
	 */
	@Test
	public void  testExists() {
		boolean exists = customerDao.existsById(4l);
		System.out.println("id为4的客户 是否存在："+exists);
	}



	/**
	 * 根据id从数据库查询
	 *      @Transactional : 保证getOne正常运行
	 *
	 *  findById：
	 *      em.find()           :立即加载
	 *  getOne：
	 *      em.getReference     :延迟加载
	 *      * 返回的是一个客户的动态代理对象
	 *      * 什么时候用，什么时候查询
	 */
	@Test
	@Transactional
	public void  testGetOne() {
		Optional<Customer> customerOptional = customerDao.findById(2l);
		System.out.println(customerOptional.get());
		Customer customer = customerDao.getOne(2l);
		System.out.println(customer);
	}


	//测试方法命名规则的查询
	@Test
	public void testFindByCustNameLike() {
		List<Customer> list = customerDao.findByCustNameLike("ziyin%");
		for (Customer customer : list) {
			System.out.println(customer);
		}
	}

	//测试方法命名规则的查询
	@Test
	public void testFindByCustNameLikeAndCustIndustry() {
		Customer customer = customerDao.findByCustNameLikeAndCustIndustry("ziyin%", "it公司");
		System.out.println(customer);
	}

}
