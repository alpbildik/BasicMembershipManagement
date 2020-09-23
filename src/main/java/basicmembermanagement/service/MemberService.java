package basicmembermanagement.service;

import basicmembermanagement.entity.Member;
import basicmembermanagement.repository.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberRepo memberRepository;

    public void save(Member member) {
        try {
            memberRepository.save(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Member member) {
        try {
            member.setStatus(0L);
            memberRepository.save(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Member> findAll() {
        return memberRepository.findAll().stream()
                .filter(member -> member.getStatus().equals(1L))
                .collect(Collectors.toList());
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }
}
