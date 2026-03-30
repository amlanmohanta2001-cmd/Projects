"""
Setup Script - Download YouTube Trending Data from Kaggle
Run this ONCE to download the dataset using kagglehub
"""

import os
import shutil
import kagglehub


def setup_data():
    """
    Download YouTube trending videos dataset from Kaggle using kagglehub.
    """
    print("\n" + "="*80)
    print("YOUTUBE TRENDING VIDEO STATISTICS - DATA SETUP")
    print("="*80)
    print("\nDownloading YouTube Trending Videos dataset from Kaggle...")
    print("This may take a few minutes on first run...\n")
    
    try:
        # Download the dataset
        print("\n[LOADING] Downloading from Kaggle (datasnaek/youtube-new)...")
        kagglehub_path = kagglehub.dataset_download("datasnaek/youtube-new")
        print(f"[OK] Downloaded from: {kagglehub_path}")
        
        # Get the data folder path in our project
        current_dir = os.path.dirname(os.path.abspath(__file__))
        data_folder = os.path.join(current_dir, 'data')
        
        # Copy CSV files from downloaded location to our data folder
        print(f"\n[LOADING] Copying CSV files to {data_folder}...")
        
        # Find all CSV files in the downloaded location
        csv_files = [f for f in os.listdir(kagglehub_path) if f.endswith('.csv')]
        
        if not csv_files:
            raise FileNotFoundError("No CSV files found in downloaded dataset")
        
        # Create data folder if it doesn't exist
        os.makedirs(data_folder, exist_ok=True)
        
        # Copy CSV files
        for csv_file in csv_files:
            src_path = os.path.join(kagglehub_path, csv_file)
            dst_path = os.path.join(data_folder, csv_file)
            shutil.copy2(src_path, dst_path)
            print(f"  [OK] Copied: {csv_file}")
        
        print("\n" + "="*80)
        print(f"[OK] SUCCESS! Downloaded {len(csv_files)} CSV files")
        print("="*80)
        print("\nYou can now run the program:")
        print("  python src/main.py")
        print("\n" + "="*80 + "\n")
        
        return True
    
    except ImportError:
        print("\n[ERROR] kagglehub is not installed")
        print("Install it with: python -m pip install -r requirements.txt\n")
        return False
    
    except Exception as e:
        print(f"\n[ERROR] {str(e)}")
        print("\nTroubleshooting:")
        print("1. Make sure you have kagglehub installed: python -m pip install -r requirements.txt")
        print("2. Make sure you have internet connection")
        print("3. Visit https://www.kaggle.com/settings/account and verify your account")
        print("4. Check if Kaggle API is configured: ~/.kaggle/kaggle.json")
        print("\n")
        return False


if __name__ == "__main__":
    success = setup_data()
    exit(0 if success else 1)
