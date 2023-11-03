/*
 * 개인프로젝트 - MemoDao.java
 * 개발자 : 컴퓨터공학과 20193037 김권수
 * 20193037@office.deu.ac.kr
 */

package deu.soft.a20193037;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MemoDao {

    @Query("SELECT * FROM memo20193037 ORDER BY created_date desc")
    List<MemoEntity> getAllMemos();

    @Query("SELECT * FROM memo20193037 ORDER BY created_date asc")
    List<MemoEntity> getAllMemosDesc();

    // 검색 SQL 문
    // 매개변수 : 검색 내용
    // 반환값 : 해당 검색 내용에 일치하는 메모 리스트
    @Query("SELECT * FROM memo20193037 WHERE title LIKE '%'||:search||'%' OR content LIKE '%'||:search||'%'")
    public List<MemoEntity> searchMemo(String search);

    // 삽입 SQL 문
    // 매개변수 : 저장할 메모의 제목, 내용
    @Query("INSERT INTO memo20193037(title,content) VALUES(:title, :content)")
    public void createMemo(String title, String content);

    // 저장된 메모를 불러오는 SQL 문
    // 매개변수 : 불러올 메모 id
    // 반환값 : 해당 메모의 튜플
    @Query("SELECT * FROM memo20193037 WHERE id =:id")
    public MemoEntity loadMemo(int id);

    // 삭제 SQL
    // 매개변수 : 삭제할 메모 id
    @Query("DELETE FROM memo20193037 WHERE id=:id")
    public void deleteMemo(int id);

    // 수정 SQL 문
    // 매개변수 : 수정한 메모 제목, 내용, 수정할 메모 id
    @Query("UPDATE memo20193037 SET title=:title, content=:content, last_modified_date=CURRENT_TIMESTAMP WHERE id=:id")
    public void updateMemo(String title,String content,int id);

    // 모두 삭제 SQL
    @Query("DELETE FROM memo20193037")
    public void deleteAllMemo();
}
