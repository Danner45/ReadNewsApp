package project.an.readnewsapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import project.an.readnewsapp.Fragment.NewsListFragment;
import project.an.readnewsapp.Fragment.RandomeTopicFragment;
import project.an.readnewsapp.Models.Categories;

public class CategoryViewPageAdapter extends FragmentStateAdapter {
    private final List<Categories> categories;

    public CategoryViewPageAdapter(@NonNull Fragment fragment, List<Categories> categories) {
        super(fragment);
        this.categories = categories != null ? categories : new ArrayList<>();// Đảm bảo categories không null
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Kiểm tra rằng vị trí hợp lệ
        if (position == 0) return new RandomeTopicFragment();
        if (position > 0 && position < categories.size()) {
            if (categories.get(position).getUrl()!=null){
                String url = categories.get(position).getUrl();
                String name = categories.get(position).getTitle();
                return NewsListFragment.newInstance(url, name);
            }
            else return new RandomeTopicFragment();
        } else {
            // Nếu vị trí không hợp lệ, trả về một Fragment mặc định hoặc thông báo lỗi
            return new RandomeTopicFragment(); // Hoặc xử lý lỗi khác
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
