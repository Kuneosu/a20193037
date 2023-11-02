/*
 * 개인프로젝트 - MemoEntity.java
 * 개발자 : 컴퓨터공학과 20193037 김권수
 * 20193037@office.deu.ac.kr
 */

package deu.soft.a20193037;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// 테이블 명을 memo20193037로 지정
@Entity(tableName = "memo20193037")
public class MemoEntity {
    // MemoEntity 라는 이름의 테이블 구조체 선언
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;

    public String content;

    @ColumnInfo(name = "created_date", defaultValue = "CURRENT_TIMESTAMP")
    public String createdDate;

    @ColumnInfo(name = "last_modified_date", defaultValue = "null")
    public String lastModifiedDate;
}