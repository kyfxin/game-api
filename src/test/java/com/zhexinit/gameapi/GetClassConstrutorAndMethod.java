package com.zhexinit.gameapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

import com.mysql.cj.xdevapi.Collection;

public class GetClassConstrutorAndMethod {
	/**
	 * @param args
	 * @author liuyue
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//使用Proxy.getProxtClass(loader,class)方法来获取一个类的字节码
		Class<?> clazzCollection = Proxy.getProxyClass(Collection.class.getClassLoader(), Collection.class);
		System.out.println("clazzCollection.name=" + clazzCollection.getName());
		
		
		System.out.println("-------there are Cons--------");	//为了打印结果好看, 添加了一个标题
		ArrayList<String> LotOfCons = getConstructors(clazzCollection);	//创建一个数组来保存返回的构造方法
		for(int i = 0; i<LotOfCons.size(); i++){	//由于直接打印ArrayList很不好看, 所有的都显示成一行, 所以循环打印
			System.out.println((i+1)+" "+LotOfCons.get(i));
		}
		
		System.out.println();
		
		System.out.println("-------there are Mets--------");	//同上, 只不过这是方法
		ArrayList<String> LotOfMets = getMethods(clazzCollection);
		for(int i = 0; i<LotOfMets.size(); i++){
			System.out.println((i+1)+" "+LotOfMets.get(i));
		}
	}
	
	
	//返回构造方法的方法, 要求传递一个Class类型的参数, 譬如通过Proxy.getProxyClass()获取的值;
	public static ArrayList<String> getConstructors(Class<?> clazzYouWant){
		
		Constructor[] cons = clazzYouWant.getConstructors();	//通过反射获得构造方法
		ArrayList<String> al = new ArrayList<String>();
		
		for(Constructor<?> con : cons){	//遍历获得的构造方法们, 对每一个构造方法获取参数
			String ConstructorName = con.getName();
			System.out.println("ConstructorName=" + ConstructorName);
			StringBuilder sb = new StringBuilder();
			//构造方法返回的是一长串字符, 像这样com.sun.proxy.$Proxy0(interface java.lang.reflect.InvocationHandler)
			//很不爽, 于是截取$符号之后的字符作为构造方法名称
			ConstructorName = ConstructorName.substring(ConstructorName.lastIndexOf("$")+1,ConstructorName.length());
 
			sb.append(ConstructorName);	//拼接字符串形成function(param,param)这种形式
			sb.append('(');
			
			Class[] clazzParams = con.getParameterTypes();	//获取参数们
			for(Class<?> clazzParam : clazzParams){	//把参数拼接到字符串内, 并在每个参数后加逗号.
				sb.append(clazzParam);
				sb.append(',');
			}
			
			//最后一个逗号是多余的, 删除掉. 判断条件是clazzParams不为空或者clazzParams的长度不为0, 则删除末尾的字符.
			// 但是使用||不对, 在没有参数的时候会删除掉小括号的前半部分; 应该使用&&就没问题了, 不知道为什么, 谁解答一下?
			if(clazzParams!=null||clazzParams.length !=0){	
				sb.deleteCharAt(sb.length()-1);	
			}
			sb.append(')');
			al.add(sb.toString());
		}
		
		return al;
	}
	
	
	//返回类的所有方法的方法, 完全同上
	public static ArrayList<String> getMethods(Class<?> clazzYouWant){
		
		Method[] cons = clazzYouWant.getMethods();
		ArrayList<String> al = new ArrayList<String>();
		
		for(Method con : cons){
			String name = con.getName();
			Class[] clazzParams = con.getParameterTypes();
			StringBuilder sb = new StringBuilder();
			
			sb.append(name);
			sb.append('(');
			
			for(Class<?> clazzParam : clazzParams){
				sb.append(clazzParam.getName());
				sb.append(',');
			}
			
			if(clazzParams!=null && clazzParams.length !=0){
				sb.deleteCharAt(sb.length()-1);
			}
			sb.append(')');
			al.add(sb.toString());
		}
		
		return al;
	}
}
