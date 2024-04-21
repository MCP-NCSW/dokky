package com.mcp.semi.mypage.controller;

import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.mcp.semi.mypage.service.MyPageService;
import com.mcp.semi.user.dto.UserDto;

import com.mcp.semi.board.dto.BoardDto;
import lombok.RequiredArgsConstructor;

@Controller
@SessionAttributes({ "loginUser" })
@RequestMapping("dokky")
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService myPageService;

	/**
	 * 회원 정보 조회
	 * 
	 * @param model
	 * @return forward (myProfile.jsp)
	 */
	@GetMapping("mypage/{userNo}")
	public String myProfile(@PathVariable("userNo") int userNo, Model model) {
		UserDto userProfile = myPageService.getUserProfile(userNo);
		model.addAttribute("user", userProfile);
		return "mypage/myProfile";
	}

	/**
	 * 비밀번호 변경
	 * 
	 * @return forward (modifyPw.jsp)
	 */
	@GetMapping("modify-password")
	public String modifyPw() {
		return "mypage/modifyPw";
	}

	/**
	 * 비밀번호 변경
	 * 
	 * @param pwMap
	 * @param loginUser
	 * @return redirect (myProfile() or modifyPw())
	 */
	@PostMapping("modify-password/{userNo}")
	public String modifyPw(@RequestParam Map<String, Object> pwMap, @PathVariable("userNo") int userNo) {

		pwMap.put("userNo", userNo);
		int result = myPageService.modifyPw(pwMap);

		if (result == 1)
			return "redirect:mypage";
		else
			return "redirect:modify-password";

	}

	/**
	 * 계정 삭제
	 * 
	 * @return forward (removeConfirm.jsp)
	 */
	@GetMapping("remove-confirm")
	public String removeConfirm() {
		return "mypage/removeConfirm";
	}

	// 내가 작성한 글 조회
	@GetMapping(value = "/api/my-board/{userNo}", produces = "application/json")
	public ResponseEntity<?> myBoard(@PathVariable("userNo") int userNo) {
		List<BoardDto> boardList = myPageService.getUserBoards(userNo);
		if (boardList.isEmpty()) {
			return ResponseEntity.ok(Map.of("message", "아직 작성한 게시글이 없습니다"));
		} else {
			return ResponseEntity.ok(boardList);
		}
	}

	// 내가 댓글 단 게시글 정보 + 댓글 내용 조회
	@GetMapping(value = "/api/my-comment/{userNo}", produces = "application/json")
	public ResponseEntity<?> myComment(@PathVariable("userNo") int userNo) {
		List<BoardDto> boardList = myPageService.getUserBoardsWithComments(userNo);
		if (boardList.isEmpty()) {
			return ResponseEntity.ok(Map.of("message", "아직 작성한 댓글이 없습니다"));
		} else {
			return ResponseEntity.ok(boardList);	
		}
		
	}

}
