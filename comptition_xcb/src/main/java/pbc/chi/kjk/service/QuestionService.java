package pbc.chi.kjk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import pbc.chi.kjk.dao.QuestionDao;
import pbc.chi.kjk.pojo.Question;

@Repository
public class QuestionService implements QuestionDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * flag表示题型，status=0表示未抽取 status=0表示未抽取 
	 */
	@Override
	public List<Question> getQuestionList(Integer flag, Integer status) {
		List<Question> questionList = jdbcTemplate.query("select * from question where flag = ? and status = ? order by que_num asc", new Object[] {flag,status},
				new BeanPropertyRowMapper<>(Question.class));
		if (questionList != null && questionList.size() > 0) {
			return questionList;
		} else {
			return null;
		}
	}
	
	
	@Override
	public Question findQuestion(int flag, int que_num) {
		List<Question> questionList = jdbcTemplate.query("select * from question where que_num=? and flag=? ", new Object[] {que_num,flag},
				new BeanPropertyRowMapper<>(Question.class));
		if (questionList != null && questionList.size() > 0) {
			return questionList.get(0);
		} else {
			return null;
		}
	}
	
	public List<Question> getQuestionAllList() {
		List<Question> questionList = jdbcTemplate.query("select * from question ", new Object[] {},
				new BeanPropertyRowMapper<>(Question.class));
		if (questionList != null && questionList.size() > 0) {
			return questionList;
		} else {
			return null;
		}
	}
	
	@Override
	public int add(Question qu) {
		jdbcTemplate.update("insert into question(que_num,que_title,que_answer,status,backtime,flag,fenzhi) "
			+ "values(?, ?, ?, ?, ?, ?, ?)", qu.getQue_num(),qu.getQue_title(),qu.getQue_answer(),qu.getStatus(),qu.getBacktime(),qu.getFlag(),qu.getFenzhi());
		return this.getQuestionAllList().size();
	}
	
	@Override
	public int update(Question qu) {
		return jdbcTemplate.update("update question set status = 1 where que_id=? ",  qu.getQue_id());
	}

	@Override
	public int initdata(Question qu) {
		return jdbcTemplate.update("update question set status = 0 where que_id=? ",  qu.getQue_id());
	}
	
	@Override
	public int deldata() {
		return jdbcTemplate.update("delete from question");
	}
}
