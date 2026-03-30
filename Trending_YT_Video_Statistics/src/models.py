"""
Video Data Model
Simple data class to represent a YouTube trending video
"""

class Video:
    """
    Simple data container for a YouTube video.
    Stores basic information about a trending video.
    """
    
    def __init__(self, video_id, title, channel_name, views, likes, comments, publish_date):
        """
        Initialize a Video object.
        
        Args:
            video_id (str): Unique identifier for the video
            title (str): Title of the video
            channel_name (str): Name of the channel that published it
            views (int): Number of views
            likes (int): Number of likes
            comments (int): Number of comments
            publish_date (str): Date when video was published
        """
        self.video_id = video_id
        self.title = title
        self.channel_name = channel_name
        self.views = int(views) if views else 0
        self.likes = int(likes) if likes else 0
        self.comments = int(comments) if comments else 0
        self.publish_date = publish_date
    
    def get_engagement_rate(self):
        """
        Calculate engagement rate (likes / views * 100).
        
        Returns:
            float: Engagement rate as percentage
        """
        if self.views == 0:
            return 0.0
        return (self.likes / self.views) * 100
    
    def get_comment_to_like_ratio(self):
        """
        Calculate comment to like ratio.
        
        Returns:
            float: Ratio of comments to likes
        """
        if self.likes == 0:
            return 0.0
        return self.comments / self.likes
    
    def __str__(self):
        """Return string representation of video."""
        return f"{self.title} by {self.channel_name} ({self.views} views)"
    
    def __repr__(self):
        """Return detailed representation of video."""
        return f"Video(id={self.video_id}, title={self.title[:30]}..., views={self.views})"
