package com.example.ie_um.community.application;

import com.example.ie_um.community.api.dto.request.PostCreateReqDto;
import com.example.ie_um.community.api.dto.request.PostUpdateReqDto;
import com.example.ie_um.community.api.dto.response.PostResDto;
import com.example.ie_um.community.domain.Post;
import com.example.ie_um.community.domain.PostLike;
import com.example.ie_um.community.exception.PostNotFoundException;
import com.example.ie_um.community.exception.UnauthorizedAccessException;
import com.example.ie_um.community.repository.PostLikeRepository;
import com.example.ie_um.community.repository.PostRepository;
import com.example.ie_um.member.domain.Member;
import com.example.ie_um.member.exception.MemberNotFoundException;
import com.example.ie_um.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public PostResDto createPost(PostCreateReqDto reqDto, Long memberId) {
        Member author = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        Post post = new Post(reqDto.getResourceId(), reqDto.getTitle(), reqDto.getContent(), author);
        Post savedPost = postRepository.save(post);
        return PostResDto.builder()
                .postId(savedPost.getId())
                .resourceId(savedPost.getResourceId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .memberId(savedPost.getAuthor().getId())
                .memberNickname(savedPost.getAuthor().getNickName())
                .likesCount(0)
                .createDate(savedPost.getCreateDate())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PostResDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> PostResDto.builder()
                        .postId(post.getId())
                        .resourceId(post.getResourceId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .memberId(post.getAuthor().getId())
                        .memberNickname(post.getAuthor().getNickName())
                        .likesCount(post.getLikesCount())
                        .createDate(post.getCreateDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        return PostResDto.builder()
                .postId(post.getId())
                .resourceId(post.getResourceId())
                .title(post.getTitle())
                .content(post.getContent())
                .memberId(post.getAuthor().getId())
                .memberNickname(post.getAuthor().getNickName())
                .likesCount(post.getLikesCount())
                .createDate(post.getCreateDate())
                .build();
    }

    @Transactional
    public PostResDto updatePost(Long postId, PostUpdateReqDto reqDto, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        if (!post.getAuthor().getId().equals(memberId)) {
            throw new UnauthorizedAccessException("게시글을 수정할 권한이 없습니다.");
        }

        post.update(reqDto.getTitle(), reqDto.getContent());
        return PostResDto.builder()
                .postId(post.getId())
                .resourceId(post.getResourceId())
                .title(post.getTitle())
                .content(post.getContent())
                .memberId(post.getAuthor().getId())
                .memberNickname(post.getAuthor().getNickName())
                .likesCount(post.getLikesCount())
                .createDate(post.getCreateDate())
                .build();
    }

    @Transactional
    public void deletePost(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        if (!post.getAuthor().getId().equals(memberId)) {
            throw new UnauthorizedAccessException("게시글을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    @Transactional
    public void likePost(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        boolean alreadyLiked = postLikeRepository.findByPostIdAndMemberId(postId, memberId).isPresent();
        if (!alreadyLiked) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
            PostLike postLike = new PostLike(post, member);
            postLikeRepository.save(postLike);
        }
    }

    @Transactional
    public void unlikePost(Long postId, Long memberId) {
        PostLike postLike = postLikeRepository.findByPostIdAndMemberId(postId, memberId)
                .orElseThrow(() -> new PostNotFoundException("좋아요를 찾을 수 없습니다."));

        postLikeRepository.delete(postLike);
    }
}
