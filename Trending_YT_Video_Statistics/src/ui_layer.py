"""
UI Layer - Console Display and Formatting
Handles all console output and user interaction
"""

from tabulate import tabulate


def display_trending_videos(videos, count=20):
    """
    Display trending videos in a formatted table.
    
    Args:
        videos (list): List of Video objects
        count (int): Number of videos to display
    """
    if not videos:
        print("No videos to display.")
        return
    
    videos_to_show = videos[:count]
    
    # Prepare table data
    table_data = []
    for i, video in enumerate(videos_to_show, 1):
        table_data.append([
            i,
            video.title[:40] + "..." if len(video.title) > 40 else video.title,
            video.channel_name[:20] + "..." if len(video.channel_name) > 20 else video.channel_name,
            f"{video.views:,}",
            f"{video.likes:,}",
            f"{video.get_engagement_rate():.2f}%"
        ])
    
    headers = ["#", "Title", "Channel", "Views", "Likes", "Engagement %"]
    print("\n" + "="*140)
    print(f"TOP {count} TRENDING VIDEOS")
    print("="*140)
    print(tabulate(table_data, headers=headers, tablefmt="grid"))
    print("="*140 + "\n")


def display_engagement_analysis(videos, top_n=15):
    """
    Display videos ranked by engagement rate.
    
    Args:
        videos (list): List of Video objects
        top_n (int): Number of top videos to show
    """
    if not videos:
        print("No videos to analyze.")
        return
    
    # Sort by engagement rate
    sorted_videos = sorted(videos, key=lambda v: v.get_engagement_rate(), reverse=True)
    top_videos = sorted_videos[:top_n]
    
    # Prepare table data
    table_data = []
    for i, video in enumerate(top_videos, 1):
        table_data.append([
            i,
            video.title[:35] + "..." if len(video.title) > 35 else video.title,
            video.channel_name,
            f"{video.views:,}",
            f"{video.likes:,}",
            f"{video.comments:,}",
            f"{video.get_engagement_rate():.2f}%",
            f"{video.get_comment_to_like_ratio():.3f}"
        ])
    
    headers = ["#", "Title", "Channel", "Views", "Likes", "Comments", "Engagement %", "Comment/Like"]
    print("\n" + "="*160)
    print(f"TOP {top_n} VIDEOS BY ENGAGEMENT RATE")
    print("="*160)
    print(tabulate(table_data, headers=headers, tablefmt="grid"))
    print("="*160 + "\n")


def display_statistics(stats):
    """
    Display overall statistics summary.
    
    Args:
        stats (dict): Statistics dictionary from service_layer
    """
    print("\n" + "="*80)
    print("OVERALL STATISTICS SUMMARY")
    print("="*80)
    
    # Statistics rows
    stat_data = [
        ["Total Videos Analyzed", f"{stats['total_videos']:,}"],
        ["Total Views (All Videos)", f"{stats['total_views']:,.0f}"],
        ["Total Likes (All Videos)", f"{stats['total_likes']:,.0f}"],
        ["Average Views per Video", f"{stats['avg_views']:,.0f}"],
        ["Average Likes per Video", f"{stats['avg_likes']:,.0f}"],
        ["Average Engagement Rate", f"{stats['avg_engagement']:.2f}%"],
        ["Highest Engagement Rate", f"{stats['max_engagement']:.2f}%"],
        ["Lowest Engagement Rate", f"{stats['min_engagement']:.2f}%"],
        ["Most Viewed Video", f"{stats['max_views']:,.0f} views"],
        ["Least Viewed Video", f"{stats['min_views']:,.0f} views"],
    ]
    
    print(tabulate(stat_data, headers=["Metric", "Value"], tablefmt="grid"))
    print("="*80 + "\n")


def display_top_channels(top_channels, limit=10):
    """
    Display top channels by trending video count.
    
    Args:
        top_channels (list): List of tuples (channel_name, count)
        limit (int): Number of channels to display
    """
    if not top_channels:
        print("No channel data available.")
        return
    
    channels_to_show = top_channels[:limit]
    
    # Prepare table data
    table_data = []
    for i, (channel, count) in enumerate(channels_to_show, 1):
        table_data.append([i, channel, count])
    
    headers = ["#", "Channel Name", "Trending Videos"]
    print("\n" + "="*80)
    print(f"TOP {limit} CHANNELS BY TRENDING VIDEO COUNT")
    print("="*80)
    print(tabulate(table_data, headers=headers, tablefmt="grid"))
    print("="*80 + "\n")


def display_main_menu():
    """Display the main menu options."""
    print("\n" + "="*80)
    print("YOUTUBE TRENDING VIDEO STATISTICS ANALYZER")
    print("="*80)
    print("\n1. View Trending Videos (by views)")
    print("2. View Engagement Analysis")
    print("3. View Overall Statistics")
    print("4. View Top Channels")
    print("5. Exit")
    print("\n" + "="*80 + "\n")


def get_user_choice():
    """
    Get user menu choice.
    
    Returns:
        str: User's menu choice
    """
    choice = input("Enter your choice (1-5): ").strip()
    return choice


def display_error(error_message):
    """
    Display an error message.
    
    Args:
        error_message (str): Error message to display
    """
    print(f"\n[ERROR] {error_message}\n")


def display_success(message):
    """
    Display a success message.
    
    Args:
        message (str): Success message to display
    """
    print(f"\n[OK] {message}\n")


def display_loading():
    """Display a loading message."""
    print("\n[LOADING] Loading data...\n")


def display_goodbye():
    """Display goodbye message."""
    print("\n" + "="*80)
    print("Thank you for using YouTube Trending Video Statistics Analyzer!")
    print("="*80 + "\n")
