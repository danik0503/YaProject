package danil.enrollment.repository;

import egor.enrollment.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, String> {
    Item findByParentId(String id);
}
