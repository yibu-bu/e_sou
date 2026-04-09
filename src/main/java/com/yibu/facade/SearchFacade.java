package com.yibu.facade;

import com.yibu.common.PageResult;
import com.yibu.common.ResultCodeEnum;
import com.yibu.constant.ResourceTypeEnum;
import com.yibu.datasource.*;
import com.yibu.domain.entity.Picture;
import com.yibu.domain.entity.Post;
import com.yibu.domain.entity.User;
import com.yibu.domain.vo.SearchResultVO;
import com.yibu.exception.BusinessException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 搜索门面
 */
@Component
public class SearchFacade {

    @Resource
    private UserDataSource userDataSource;
    @Resource
    private PostDataSource postDataSource;
    @Resource
    private PictureDataSource pictureDataSource;
    @Resource
    private DataSourceRegistry dataSourceRegistry;

    /**
     * 聚合搜索
     *
     * @param searchText 搜索关键词
     * @param pageNum    页码
     * @param pageSize   单页大小
     * @param type       资源类型
     */
    public SearchResultVO search(String searchText, Integer pageNum, Integer pageSize, String type) {
        SearchResultVO searchResultVO = new SearchResultVO();
        ResourceTypeEnum resourceTypeEnum = ResourceTypeEnum.getEnumByValue(type);
        // 如果不是查询全部，就只查询具体的资源类型
        if (!resourceTypeEnum.equals(ResourceTypeEnum.ALL)) {
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            PageResult<?> pageResult = dataSource.getData(searchText, pageNum, pageSize);
            searchResultVO.setDataPageResult(pageResult);
        } else {
            // 查询全部，创建三个任务异步执行
            CompletableFuture<PageResult<User>> userTask = CompletableFuture.supplyAsync(
                    () -> userDataSource.getData(searchText, pageNum, pageSize)
            );
            CompletableFuture<PageResult<Post>> PostTask = CompletableFuture.supplyAsync(
                    () -> postDataSource.getData(searchText, pageNum, pageSize) // 帖子从ES里获取
            );
            CompletableFuture<PageResult<Picture>> PictureTask = CompletableFuture.supplyAsync(
                    () -> pictureDataSource.getData(searchText, pageNum, pageSize)
            );
            CompletableFuture.allOf(userTask, PostTask, PictureTask).join(); // 等待所有任务完成
            try {
                searchResultVO.setUserPageResult(userTask.get());
                searchResultVO.setPostPageResult(PostTask.get());
                searchResultVO.setPicturePageResult(PictureTask.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new BusinessException(ResultCodeEnum.SYSTEM_ERROR, e.getMessage());
            }
        }
        return searchResultVO;
    }

    /**
     * 不查询图片
     */
    public SearchResultVO searchWithoutPicture(String searchText, Integer pageNum, Integer pageSize, String type) {
        SearchResultVO searchResultVO = new SearchResultVO();
        ResourceTypeEnum resourceTypeEnum = ResourceTypeEnum.getEnumByValue(type);
        // 如果不是查询全部，就只查询具体的资源类型
        if (!resourceTypeEnum.equals(ResourceTypeEnum.ALL)) {
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            PageResult<?> pageResult = dataSource.getData(searchText, pageNum, pageSize);
            searchResultVO.setDataPageResult(pageResult);
        } else {
            // 查询全部，创建三个任务异步执行
            CompletableFuture<PageResult<User>> userTask = CompletableFuture.supplyAsync(
                    () -> userDataSource.getData(searchText, pageNum, pageSize)
            );
            CompletableFuture<PageResult<Post>> PostTask = CompletableFuture.supplyAsync(
                    () -> postDataSource.getData(searchText, pageNum, pageSize) // 帖子从ES里获取
            );
            CompletableFuture.allOf(userTask, PostTask).join(); // 等待所有任务完成
            try {
                searchResultVO.setUserPageResult(userTask.get());
                searchResultVO.setPostPageResult(PostTask.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new BusinessException(ResultCodeEnum.SYSTEM_ERROR, e.getMessage());
            }
        }
        return searchResultVO;
    }

    /**
     * 用于测试不用使用并发异步任务的性能
     */
    public SearchResultVO searchOld(String searchText, Integer pageNum, Integer pageSize, String type) {
        SearchResultVO searchResultVO = new SearchResultVO();
        ResourceTypeEnum resourceTypeEnum = ResourceTypeEnum.getEnumByValue(type);
        // 如果不是查询全部，就只查询具体的资源类型
        if (!resourceTypeEnum.equals(ResourceTypeEnum.ALL)) {
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            PageResult<?> pageResult = dataSource.getData(searchText, pageNum, pageSize);
            searchResultVO.setDataPageResult(pageResult);
        } else {
            // 查询全部
            searchResultVO.setUserPageResult(userDataSource.getData(searchText, pageNum, pageSize));
            searchResultVO.setPostPageResult(postDataSource.getData(searchText, pageNum, pageSize));
//            searchResultVO.setPicturePageResult(pictureDataSource.getData(searchText, pageNum, pageSize));
        }
        return searchResultVO;
    }
}
