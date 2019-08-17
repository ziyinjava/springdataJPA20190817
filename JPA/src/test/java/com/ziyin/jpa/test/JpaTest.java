package com.ziyin.jpa.test;

import com.ziyin.bo.Customer;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author ziyin
 * @create 2019-08-08 10:42
 */
public class JpaTest {

	/**
	 * 测试jpa的保存
	 *      案例：保存一个客户到数据库中
	 *  Jpa的操作步骤
	 *     1.加载配置文件创建工厂（实体管理器工厂）对象
	 *     2.通过实体管理器工厂获取实体管理器
	 *     3.获取事务对象，开启事务
	 *     4.完成增删改查操作
	 *     5.提交事务（回滚事务）
	 *     6.释放资源
	 */
	@Test
	public void testSave(){
       //1.加载配置文件创建工厂（实体管理器工厂）对象
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myJpa");
		//2.通过实体管理器工厂获取实体管理器
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		//3.获取事务对象，开启事务
		//获取事务对象
		EntityTransaction transaction = entityManager.getTransaction();
		//开启事务
		transaction.begin();
		//4.完成增删改查操作：保存一个客户到数据库中
		Customer customer = new Customer();
		customer.setCustName("ziyin");
		customer.setCustIndustry("科技");
		//保存，
		entityManager.persist(customer); //保存操作
		//5.提交事务
		transaction.commit();
		//6.释放资源
		entityManager.close();
		entityManagerFactory.close();
	}
}
