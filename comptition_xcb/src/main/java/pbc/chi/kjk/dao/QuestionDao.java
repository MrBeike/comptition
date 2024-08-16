package pbc.chi.kjk.dao;

import java.util.List;

import pbc.chi.kjk.pojo.Question;

public interface QuestionDao {
	
	public List<Question> getQuestionList(Integer flag, Integer status) ;
	public Question findQuestion(int flag,int que_num);
	public int add(Question qu);
	public int update(Question qu);
	public int initdata(Question qu);
	public int deldata();

}
