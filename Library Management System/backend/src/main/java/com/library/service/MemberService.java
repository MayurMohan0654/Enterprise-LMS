package com.library.service;

import com.library.model.Member;
import com.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    
    @Autowired
    private MemberRepository memberRepository;
    
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
    
    public Optional<Member> getMemberById(Integer id) {
        return memberRepository.findById(id);
    }
    
    public Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
    
    public List<Member> searchMembersByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name);
    }
    
    public Member addMember(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalArgumentException("Member with email " + member.getEmail() + " already exists");
        }
        
        // Set membership date to today if not provided
        if (member.getMembershipDate() == null) {
            member.setMembershipDate(LocalDate.now());
        }
        
        return memberRepository.save(member);
    }
    
    public Member updateMember(Integer id, Member memberDetails) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Member not found with id " + id));
        
        // Check if email is changed and not already in use
        if (!member.getEmail().equals(memberDetails.getEmail())) {
            if (memberRepository.existsByEmail(memberDetails.getEmail())) {
                throw new IllegalArgumentException("Member with email " + memberDetails.getEmail() + " already exists");
            }
        }
        
        member.setName(memberDetails.getName());
        member.setEmail(memberDetails.getEmail());
        member.setPhone(memberDetails.getPhone());
        member.setAddress(memberDetails.getAddress());
        
        return memberRepository.save(member);
    }
    
    public void deleteMember(Integer id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Member not found with id " + id));
        memberRepository.delete(member);
    }
}
