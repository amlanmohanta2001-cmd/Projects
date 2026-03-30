"""
Service Layer - Business Logic and Analysis
Performs calculations and analysis on video data
"""

from collections import Counter
from datetime import datetime


def calculate_engagement_rate(likes, views):
    """
    Calculate engagement rate.
    
    Formula: (Likes / Views) * 100
    
    Args:
        likes (int): Number of likes
        views (int): Number of views
        
    Returns:
        float: Engagement rate as percentage
    """
    if views == 0:
        return 0.0
    return (likes / views) * 100


def calculate_trending_score(views, engagement_rate):
    """
    Calculate a trending score combining views and engagement.
    
    Formula: Views * (1 + Engagement_Rate/100)
    This gives higher scores to videos with many views AND high engagement.
    
    Args:
        views (int): Number of views
        engagement_rate (float): Engagement rate percentage
        
    Returns:
        float: Trending score
    """
    return views * (1 + engagement_rate / 100)


def rank_videos_by_engagement(videos):
    """
    Rank videos by engagement rate (highest first).
    
    Args:
        videos (list): List of Video objects
        
    Returns:
        list: Videos sorted by engagement rate (descending)
    """
    return sorted(videos, key=lambda v: v.get_engagement_rate(), reverse=True)


def rank_videos_by_views(videos):
    """
    Rank videos by view count (highest first).
    
    Args:
        videos (list): List of Video objects
        
    Returns:
        list: Videos sorted by views (descending)
    """
    return sorted(videos, key=lambda v: v.views, reverse=True)


def rank_videos_by_trending_score(videos):
    """
    Rank videos by trending score (highest first).
    
    Args:
        videos (list): List of Video objects
        
    Returns:
        list: Videos sorted by trending score (descending)
    """
    scored_videos = [
        (v, calculate_trending_score(v.views, v.get_engagement_rate()))
        for v in videos
    ]
    scored_videos.sort(key=lambda x: x[1], reverse=True)
    return [v for v, score in scored_videos]


def get_top_channels(videos, limit=10):
    """
    Get the top channels by number of trending videos.
    
    Args:
        videos (list): List of Video objects
        limit (int): Number of top channels to return
        
    Returns:
        list: List of tuples (channel_name, count) sorted by count
    """
    channel_counts = Counter(v.channel_name for v in videos)
    return channel_counts.most_common(limit)


def calculate_average_engagement_rate(videos):
    """
    Calculate average engagement rate across all videos.
    
    Args:
        videos (list): List of Video objects
        
    Returns:
        float: Average engagement rate
    """
    if not videos:
        return 0.0
    
    total_engagement = sum(v.get_engagement_rate() for v in videos)
    return total_engagement / len(videos)


def calculate_average_views(videos):
    """
    Calculate average views across all videos.
    
    Args:
        videos (list): List of Video objects
        
    Returns:
        float: Average views
    """
    if not videos:
        return 0.0
    
    total_views = sum(v.views for v in videos)
    return total_views / len(videos)


def calculate_average_likes(videos):
    """
    Calculate average likes across all videos.
    
    Args:
        videos (list): List of Video objects
        
    Returns:
        float: Average likes
    """
    if not videos:
        return 0.0
    
    total_likes = sum(v.likes for v in videos)
    return total_likes / len(videos)


def filter_videos_by_min_views(videos, min_views):
    """
    Filter videos that have at least min_views.
    
    Args:
        videos (list): List of Video objects
        min_views (int): Minimum number of views
        
    Returns:
        list: Filtered videos
    """
    return [v for v in videos if v.views >= min_views]


def filter_videos_by_min_engagement(videos, min_engagement):
    """
    Filter videos that have at least min_engagement rate.
    
    Args:
        videos (list): List of Video objects
        min_engagement (float): Minimum engagement rate percentage
        
    Returns:
        list: Filtered videos
    """
    return [v for v in videos if v.get_engagement_rate() >= min_engagement]


def generate_statistics_summary(videos):
    """
    Generate a comprehensive statistics summary of the videos.
    
    Args:
        videos (list): List of Video objects
        
    Returns:
        dict: Dictionary containing various statistics
    """
    if not videos:
        return {
            'total_videos': 0,
            'avg_views': 0,
            'avg_likes': 0,
            'avg_engagement': 0,
            'total_views': 0,
            'total_likes': 0,
            'top_channels': []
        }
    
    return {
        'total_videos': len(videos),
        'avg_views': calculate_average_views(videos),
        'avg_likes': calculate_average_likes(videos),
        'avg_engagement': calculate_average_engagement_rate(videos),
        'total_views': sum(v.views for v in videos),
        'total_likes': sum(v.likes for v in videos),
        'top_channels': get_top_channels(videos, limit=10),
        'max_views': max(v.views for v in videos),
        'min_views': min(v.views for v in videos),
        'max_engagement': max(v.get_engagement_rate() for v in videos),
        'min_engagement': min(v.get_engagement_rate() for v in videos),
    }
