package com.fehead.yiban_interface.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fehead.yiban_interface.dao.dataobject.TokenDO;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 写代码 敲快乐
 * だからよ...止まるんじゃねぇぞ
 * ▏n
 * █▏　､⺍
 * █▏ ⺰ʷʷｨ
 * █◣▄██◣
 * ◥██████▋
 * 　◥████ █▎
 * 　　███▉ █▎
 * 　◢████◣⌠ₘ℩
 * 　　██◥█◣\≫
 * 　　██　◥█◣
 * 　　█▉　　█▊
 * 　　█▊　　█▊
 * 　　█▊　　█▋
 * 　　 █▏　　█▙
 * 　　 █
 *
 * @author Nightnessss 2019/9/26 9:52
 */
public interface TokenMapper extends BaseMapper<TokenDO> {

    @Select("SELECT * FROM token where " +
            "user_id=#{userId}")
    public TokenDO selectByUserId(String userId);

    @Update("UPDATE token " +
            "SET access_token=#{accessToken}, expires=#{expires} " +
            "WHERE user_id=#{userId}")
    public void updateToken(String userId, String accessToken, String expires);
}
